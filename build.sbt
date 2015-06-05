name := "sbtellar"

version := "1.0"

scalaVersion := "2.11.6"

resolvers += "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/maven-snapshots/"

resolvers += Resolver.url("Typesafe Ivy Snapshots Repository", url("http://repo.typesafe.com/typesafe/ivy-snapshots"))(Resolver.ivyStylePatterns)

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "2.5.0"

libraryDependencies += "org.bouncycastle" % "bcprov-jdk15on" % "1.51"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

libraryDependencies += "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-RC2"

libraryDependencies += "com.typesafe.akka" %% "akka-http-core-experimental" % "1.0-RC3"

libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "1.0-RC3"

libraryDependencies += "com.typesafe.slick" %% "slick" % "3.0.0"

libraryDependencies += "com.h2database" % "h2" % "1.4.186"
