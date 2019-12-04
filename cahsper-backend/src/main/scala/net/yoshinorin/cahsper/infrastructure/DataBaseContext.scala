package net.yoshinorin.cahsper.infrastructure

import io.getquill.{MysqlJdbcContext, SnakeCase}

object DataBaseContext {

  lazy val ctx = new MysqlJdbcContext[SnakeCase](SnakeCase, "db.ctx")

}

class DataBaseContext[T] {

  val ctx = DataBaseContext.ctx
}
