package net.yoshinorin.cahsper.config

import com.typesafe.config.{Config, ConfigFactory}

object Config {

  private val config: Config = ConfigFactory.load

  val dbUrl: String = config.getString("db.ctx.dataSource.url")
  val dbUser: String = config.getString("db.ctx.dataSource.user")
  val dbPassword: String = config.getString("db.ctx.dataSource.password")

}
