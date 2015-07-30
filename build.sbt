name := "sbtellar"

organization := "org.strllar"

sbtPlugin := true

version := "0.1"

scalaVersion := "2.10.5"

lazy val root = (project in file(".")).dependsOn(file("deps/scala-stellar-base"))

logLevel in run := Level.Debug
