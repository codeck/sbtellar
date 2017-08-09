//a trick from http://www.scala-sbt.org/0.13.5/docs/Extending/Plugins.html 1d) for dev use
//"addSbtPlugin("org.strllar" % "sbtellar" % "0.1")" for prod use
lazy val root = project.in( file(".") ).dependsOn( RootProject(file("../..")) )
