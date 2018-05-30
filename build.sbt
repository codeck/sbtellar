lazy val root = (project in file("."))
.settings(
    name := "sbtellar",
    organization := "org.strllar",
    sbtPlugin := true,
    version := "0.1",
    scalaVersion := "2.12.6",
    logLevel in run := Level.Debug
  ).dependsOn(ProjectRef(uri("git://github.com/strllar/scala-stellar-base.git#master"), "stellarbaseJVM"))

