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

val circeVersion = "0.13.0"
val akkaVersion = "2.6.3"
val akkaHttpVersion = "10.1.11"
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.megard" %% "akka-http-cors" % "0.4.2",
  "com.nimbusds" % "nimbus-jose-jwt" % "8.6",
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
  "io.getquill" %% "quill-jdbc" % "3.5.0",
  "org.flywaydb" % "flyway-core" % "6.2.3",
  "org.mariadb.jdbc" % "mariadb-java-client" % "2.5.4",
  "org.slf4j" % "slf4j-api" % "1.7.29",
  "org.scalatest" %% "scalatest" % "3.1.0" % "test",
  "org.mockito" % "mockito-core" % "3.2.0" % "test"
)

coverageExcludedPackages := "<empty>; net.yoshinorin.cahsper.BootStrap; net.yoshinorin.cahsper.infrastructure.Migration; net.yoshinorin.services.infrastructure.DataBaseContext; net.yoshinorin.cahsper.http.HttpServer;"
org.scoverage.coveralls.Imports.CoverallsKeys.coverallsGitRepoLocation := Some("..")
