name := """Notes-Scala-PlayFramework"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.6" //Libreria Para El Manejo de Boostrap Para Mejora Del Hmtl
libraryDependencies += "org.postgresql" % "postgresql" % "42.3.1" //Libreria Para El Manejo de La base de datos por Postgrstl
libraryDependencies ++= Seq(jdbc) //Plugin jdbc de java para el Manejo de Bases De Datos


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
