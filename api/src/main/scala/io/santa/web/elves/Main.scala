package io.santa.web.elves

import io.santa.web.elves.example.service.{Counter, CounterPostgres}
import sttp.model.StatusCode
import zio.magic._
import zio.{ExitCode, Has, URIO, ZEnv, ZIO}
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zhttp.service.Server
import sttp.tapir.ztapir._
import zhttp.http.{Http, Request, Response}

import java.net.InetAddress

object Main extends zio.App {
  private type Env = ZEnv with Has[Counter]

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

  private val program = for {
    _ <- ZIO.succeed()
    // TODO: retrieve address and port from config
    _ <- Server.start(InetAddress.getLocalHost, 5989, zioHttpApp)
  } yield ()

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    program
      .inject(
        CounterPostgres.layer,
        // TODO: provide postgres data source
      )
      .exitCode

}
