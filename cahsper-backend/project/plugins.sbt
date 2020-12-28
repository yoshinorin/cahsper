import sbt.util

logLevel := util.Level.Warn

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")
// TODO: https://github.com/pureconfig/pureconfig/pull/799
addSbtPlugin("net.ruippeixotog" % "sbt-coveralls" % "1.3.0")
