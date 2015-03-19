
name := """dothraki"""

version := "2.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "jquery-ui" % "1.11.0-1"
  "org.scalajs" %% "scalajs-pickling-play-json" % "0.3-SNAPSHOT"
)

//enablePlugins(ScalaJSPlugin)

