import sbt.Project.projectToRef

lazy val clients = Seq(dothrakiClient)
lazy val scalaV = "2.11.5"

lazy val dothrakiServer = (project in file("dothraki-server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  libraryDependencies ++= Seq(
    jdbc,
    "com.typesafe.slick" %% "slick" % "2.1.0",
    "com.typesafe.play" %% "play-slick" % "0.8.0",
    "org.webjars" % "bootstrap" % "3.2.0",
    "org.webjars" %% "webjars-play" % "2.3.0",
    "org.webjars" % "jquery-ui" % "1.11.0-1",
    "com.vmunier" %% "play-scalajs-scripts" % "0.1.0",
    "org.webjars" % "jquery" % "1.11.1"
  ),
  EclipseKeys.skipParents in ThisBuild := false).
  enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(dothrakiSharedJvm)

lazy val dothrakiClient = (project in file("dothraki-client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  sourceMapsDirectories += dothrakiSharedJs.base / "..",
  unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0"
  )).
  enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(dothrakiSharedJs)

lazy val dothrakiShared = (crossProject.crossType(CrossType.Pure) in file("dothraki-shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSPlay).
  jsSettings(sourceMapsBase := baseDirectory.value / "..")

lazy val dothrakiSharedJvm = dothrakiShared.jvm
lazy val dothrakiSharedJs = dothrakiShared.js

// loads the jvm project at sbt startup
onLoad in Global := (Command.process("project dothrakiServer", _: State)) compose (onLoad in Global).value

skip in packageJSDependencies := false