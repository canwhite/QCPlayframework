name := """qc"""
organization := "com.qc"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.12"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.17"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.qc.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.roustesImport += "com.qc.binders._"



// add by qc play-mongo
libraryDependencies += "cn.playscala" % "play-mongo_2.12" % "0.3.0"
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
