package net.yoshinorin.cahsper.infrastructure

import io.getquill.{MysqlJdbcContext, SnakeCase}
import net.yoshinorin.cahsper.models.db.Table
import net.yoshinorin.cahsper.models.request.QueryParamater

object DataBaseContext {

  lazy val ctx = new MysqlJdbcContext[SnakeCase](SnakeCase, "db.ctx")

}

class DataBaseContext[T <: Table] {

  val ctx = DataBaseContext.ctx

  import ctx._

  def filterWithQueryParam(queryParamater: QueryParamater)(implicit schemaMeta: SchemaMeta[T]): Quoted[Query[T]] = quote {
    query[T]
      .filter(_.createdAt >= lift(queryParamater.from))
      .filter(_.createdAt <= lift(queryParamater.to))
      .take(lift(queryParamater.limit))
  }

}
