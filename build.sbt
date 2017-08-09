lazy val root = (project in file("."))
.settings(
    name := "sbtellar",
    organization := "org.strllar",
    sbtPlugin := true,
    version := "0.1",
    scalaVersion := "2.12.3",
    logLevel in run := Level.Debug
  ).dependsOn(ProjectRef(file("deps/scala-stellar-base"), "stellarbaseJVM"))

