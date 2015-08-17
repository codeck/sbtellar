name := "sbtellar"

organization := "org.strllar"

sbtPlugin := true

version := "0.1"

scalaVersion := "2.10.5"

lazy val root = (project in file(".")).dependsOn(ProjectRef(file("deps/scala-stellar-base"), "stellarbaseJVM"))

logLevel in run := Level.Debug
