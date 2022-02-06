import sbt._

object Dependencies {

  private object V {
    val tapirZioHttpServer = "0.20.0-M9"
    val tapirSharedZio = "1.3.2"
    val tapirJsonZio = "0.20.0-M9"
    val tapirRefined = "0.20.0-M9"
    val zio = "1.0.13"
    val zioPrelude = "1.0.0-RC10"
    val zioMagic = "0.3.11"
    val zioConfig = "2.0.0"
    val zioLogging = "0.5.14"
    val zioZmx = "0.0.11"
    val zioJson = "0.3.0-RC3"
    val monocle = "3.1.0"
    val refined = "0.9.28"
    val quill = "3.16.0"
  }

  // Tapir
  private lazy val tapirZioHttpServer =
    "com.softwaremill.sttp.tapir" %% "tapir-zio1-http-server" % V.tapirZioHttpServer

  private lazy val tapirSharedZio =
    "com.softwaremill.sttp.shared" %% "zio1" % V.tapirSharedZio

  private lazy val tapirJsonZio =
    "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % V.tapirJsonZio excludeAll ExclusionRule(
      "dev.zio"
    )

  private lazy val tapirRefined =
    "com.softwaremill.sttp.tapir" %% "tapir-refined" % V.tapirRefined

  // ZIO
  private lazy val zio = "dev.zio" %% "zio" % V.zio
  private lazy val zioStreams = "dev.zio" %% "zio-streams" % V.zio

  private lazy val zioPrelude =
    "dev.zio" %% "zio-prelude" % V.zioPrelude excludeAll ExclusionRule(
      "dev.zio"
    )

  private lazy val zioMagic = "io.github.kitlangton" %% "zio-magic" % V.zioMagic

  // ZIO — Config
  private lazy val zioConfig = "dev.zio" %% "zio-config" % V.zioConfig

  private lazy val zioConfigMagnolia =
    "dev.zio" %% "zio-config-magnolia" % V.zioConfig

  private lazy val zioConfigRefined =
    "dev.zio" %% "zio-config-refined" % V.zioConfig

  private lazy val zioConfigTypesafe =
    "dev.zio" %% "zio-config-typesafe" % V.zioConfig

  // ZIO — Logging
  private lazy val zioLogging = "dev.zio" %% "zio-logging" % V.zioLogging

  // ZIO — Monitoring
  private lazy val zioZmx = "dev.zio" %% "zio-zmx" % V.zioZmx

  // ZIO — JSON

  private lazy val zioJson =
    "dev.zio" %% "zio-json" % V.zioJson excludeAll ExclusionRule(
      "dev.zio"
    )

  // FP
  // FP — Optics
  private lazy val monocle = Seq(
    "dev.optics" %% "monocle-core" % V.monocle,
    "dev.optics" %% "monocle-macro" % V.monocle
  )

  // FP — Refinement types
  private lazy val refined = "eu.timepit" %% "refined" % V.refined

  // DB
  // DB — Quill
  private lazy val quill = "io.getquill" %% "quill-jdbc-zio" % V.quill

  // DB — Postgres
  private lazy val postgresJdbc = "org.postgresql" % "postgresql" % "42.3.2"

  lazy val core: Seq[ModuleID] = Seq(
    zio,
    zioStreams,
    zioPrelude,
    refined,
    quill
  ) ++ monocle

  lazy val api: Seq[ModuleID] = Seq(
    tapirZioHttpServer,
    tapirSharedZio,
    tapirJsonZio,
    tapirRefined,
    zioMagic,
    zioConfig,
    zioConfigMagnolia,
    zioConfigRefined,
    zioConfigTypesafe,
    zioJson,
    postgresJdbc
  )

}
