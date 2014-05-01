name := "sbt-ripple"

version := "1.0"

scalaVersion := "2.10.3"

resolvers += "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/maven-snapshots/"

resolvers += Resolver.url("Typesafe Ivy Snapshots Repository", url("http://repo.typesafe.com/typesafe/ivy-snapshots"))(Resolver.ivyStylePatterns)

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "2.5.0"

libraryDependencies += "org.bouncycastle" % "bcprov-jdk16" % "1.46"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.2"

libraryDependencies += "io.spray" % "spray-can" % "1.3.1"
