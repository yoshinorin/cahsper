organization := "net.yoshinorin"
name := "cahsper"
version := "0.0.1"
scalaVersion := "2.13.0"

scalacOptions ++= Seq(
  "-Yrangepos",
  "-Ywarn-unused",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-encoding",
  "UTF-8"
)

val circeVersion = "0.12.2"
val akkaVersion = "2.6.0"
val akkaHttpVersion = "10.1.10"
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.megard" %% "akka-http-cors" % "0.4.1",
  "com.nimbusds" % "nimbus-jose-jwt" % "7.9",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe" % "config" % "1.4.0",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.getquill" %% "quill-jdbc" % "3.4.10",
  "org.flywaydb" % "flyway-core" % "6.0.6",
  "org.mariadb.jdbc" % "mariadb-java-client" % "2.5.0",
  "org.slf4j" % "slf4j-api" % "1.7.28",
  "org.scalatest" %% "scalatest" % "3.1.0" % "test",
  "org.mockito" % "mockito-core" % "3.1.0" % "test"
)

coverageExcludedPackages := "<empty>; net.yoshinorin.cahsper.BootStrap; net.yoshinorin.cahsper.services.FlywayService; net.yoshinorin.services.cahsper.QuillService; net.yoshinorin.cahsper.http.HttpServer;"
org.scoverage.coveralls.Imports.CoverallsKeys.coverallsGitRepoLocation := Some("..")
