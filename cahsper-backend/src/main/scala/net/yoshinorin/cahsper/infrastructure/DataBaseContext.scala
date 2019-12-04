package net.yoshinorin.cahsper.infrastructure

import io.getquill.{MysqlJdbcContext, SnakeCase}

class DataBaseContext {

  lazy val ctx = new MysqlJdbcContext[SnakeCase](SnakeCase, "db.ctx")

}
