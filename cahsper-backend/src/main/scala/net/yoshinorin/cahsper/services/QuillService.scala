package net.yoshinorin.cahsper.services

import io.getquill.{MysqlJdbcContext, SnakeCase}

class QuillService {

  lazy val ctx = new MysqlJdbcContext[SnakeCase](SnakeCase, "db.ctx")

}
