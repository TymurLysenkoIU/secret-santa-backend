package io.santa.web.elves

import com.typesafe.config.Config
import eu.timepit.refined.auto._
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.santa.web.elves.config.{HttpServerConfig, PostgresConfig}
import io.santa.web.elves.example.service.{Counter, CounterPostgres}
import pureconfig._
import pureconfig.generic.auto._
import eu.timepit.refined.pureconfig._
import sttp.model.StatusCode
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.ztapir._
import zhttp.http.{Http, Request, Response}
import zhttp.service.Server
import zio._
import zio.magic._

import java.net.InetAddress

object Main extends zio.App {

  private type Env =
    ZEnv with Has[Counter] with Has[HttpServerConfig] with Has[PostgresConfig]

  private val configLayer = for {
    _ <- ZLayer.succeed(())
    baseConfigSource = ConfigSource.resources("application.conf")
    localConfigSource = ConfigSource.resources("application.local.conf")
    configSource = localConfigSource.withFallback(baseConfigSource)
    config <- ZIO.fromEither(configSource.load[Config]).toLayer.map(_.get)
    httpServerConfigLayer = ZIO
      .fromEither(
        ConfigSource
          .fromConfig(config.getConfig("server.http"))
          .load[HttpServerConfig]
      )
      .toLayer
    postgresConfigLayer =
      ZIO.succeed(PostgresConfig(config.getConfig("postgres"))).toLayer
    appConfig <- httpServerConfigLayer ++ postgresConfigLayer
  } yield appConfig

  private val postgresDataSourceLayer = for {
    postgresConfig <- ZLayer.service[PostgresConfig].map(_.get)
    postgresDataSource <- DataSourceLayer.fromConfig(postgresConfig.config)
  } yield postgresDataSource

  private val zioHttpApp: Http[Env, Throwable, Request, Response] =
    ZioHttpInterpreter().toHttp(
      List(
        Endpoints
          .getCounterEndpoint
          // TODO: move logic into API service
          .zServerLogic(_ =>
            Counter
              .getCounter
              .mapBoth(
                _ => StatusCode.InternalServerError,
                x => (x, StatusCode.Ok)
              )
          ),
        Endpoints
          .incrementCounterEndpoint
          .zServerLogic(i =>
            Counter
              .incrementCounter(i)
              .mapBoth(
                _ => StatusCode.InternalServerError,
                _ => StatusCode.Ok
              )
          )
      )
    )

  // Compiler thinks that start is a dead code, because it returns Nothing, for
  // which value does not exist. Although, in zio world returning Nothing means
  // that effect never completes; runs forever
  @annotation.nowarn("msg=dead code following this construct")
  private def program = for {
    httpServerConfig <- ZIO.service[HttpServerConfig]
    _ <- Server.start(
      InetAddress.getByName(httpServerConfig.bindAddress),
      httpServerConfig.port,
      zioHttpApp
    )
  } yield ()

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    program
      .injectSome[ZEnv](
        CounterPostgres.layer,
        postgresDataSourceLayer,
        configLayer
      )
      .exitCode

}
