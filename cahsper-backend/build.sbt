organization := "net.yoshinorin"
name := "cahsper"
version := "0.0.1"
scalaVersion := "2.12.9"

scalacOptions ++= Seq(
  "-Yrangepos",
  "-Ywarn-unused",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-encoding",
  "UTF-8"
)

val circeVersion = "0.11.1"
val akkaVersion = "2.5.25"
val akkaHttpVersion = "10.1.9"
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.nimbusds" % "nimbus-jose-jwt" % "7.8",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe" % "config" % "1.3.4",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.getquill" %% "quill-jdbc" % "3.4.3",
  "org.flywaydb" % "flyway-core" % "6.0.1",
  "org.mariadb.jdbc" % "mariadb-java-client" % "2.4.3",
  "org.slf4j" % "slf4j-api" % "1.7.28",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.mockito" % "mockito-core" % "3.0.0" % "test"
)

coverageExcludedPackages := "net.yoshinorin.cahsper.BootStrap; net.yoshinorin.cahsper.services.FlywayService;"
org.scoverage.coveralls.Imports.CoverallsKeys.coverallsGitRepoLocation := Some("..")
