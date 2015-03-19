
name := "dothraki"
scalaVersion in ThisBuild := "2.11.5"

lazy val shared = project in file("dothraki-shared")
lazy val client = (project in file("dothraki-client")).dependsOn(shared).enablePlugins(ScalaJSPlugin, ScalaJSPlay)
lazy val server = (project in file("dothraki-server")).dependsOn(shared).enablePlugins(PlayScala)
lazy val root = (project in file(".")).aggregate(shared, client, server)
