organization := "net.yoshinorin"
name := "cahsper"
version := "0.0.1"
scalaVersion := "2.13.4"

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
val akkaVersion = "2.6.12"
val akkaHttpVersion = "10.2.3"
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.nimbusds" % "nimbus-jose-jwt" % "9.7",
  "ch.megard" %% "akka-http-cors" % "1.1.1",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe" % "config" % "1.4.1",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.getquill" %% "quill-jdbc" % "3.6.1",
  "org.flywaydb" % "flyway-core" % "6.5.3",
  "org.mariadb.jdbc" % "mariadb-java-client" % "2.7.2",
  "org.slf4j" % "slf4j-api" % "1.7.29",
  "org.scalatest" %% "scalatest" % "3.2.5" % "test",
  "org.mockito" % "mockito-core" % "3.7.7" % "test"
)

// skip test when create assembly (because sometimes test fails)
test in assembly := {}

coverageExcludedPackages := "<empty>; net.yoshinorin.cahsper.BootStrap; net.yoshinorin.cahsper.infrastructure.Migration; net.yoshinorin.services.infrastructure.QuillDataBaseContext; net.yoshinorin.cahsper.http.HttpServer;"
org.scoverage.coveralls.Imports.CoverallsKeys.coverallsGitRepoLocation := Some("..")
