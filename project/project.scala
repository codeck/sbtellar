import sbt.Keys._
import sbt._

object MyProjectBuild extends Build {
  lazy val project = Project("sbtellar", file(".")) // <-- Make sure to name your project what you want the module to be named
    .settings(
    name := "sbtellar",
    organization := "org.strllar",
    sbtPlugin := true,
    version := "0.1",
    scalaVersion := "2.10.5",
    logLevel in run := Level.Debug
  ).dependsOn(ProjectRef(file("deps/scala-stellar-base"), "stellarbaseJVM"))
}

