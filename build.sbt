ThisBuild / scalaVersion := "2.13.7"
ThisBuild / organization := "io.santa.web.elves"
ThisBuild / organizationName := "Santa's Web Elves"

ThisBuild / homepage := Some(
  url("https://github.com/TymurLysenkoIU/secret-santa-backend")
)

ThisBuild / description :=
  """Backend for Secret Santa web site.""".stripMargin

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalafixScalaBinaryVersion := "2.13"

lazy val commonSettings = Compiler.settings ++ Seq(
  resolvers += Opts.resolver.sonatypeSnapshots,
  Compile / doc / sources := Seq.empty // So that `sbt compile stage` works
)

lazy val root = (project in file("."))
  .dependsOn(
    core,
    api
  )
  .aggregate(
    core,
    api
  )
  .settings(commonSettings)
  .settings(
    name := "secret-santa-backend",
  )

lazy val core = (project in file("core"))
  .settings(commonSettings)
  .settings(
    name := "core",
    libraryDependencies := Dependencies.core
  )

lazy val api = (project in file("api"))
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "api",
    libraryDependencies := Dependencies.api
  )
  .enablePlugins(JavaServerAppPackaging)
