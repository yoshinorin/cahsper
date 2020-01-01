package net.yoshinorin.cahsper.infrastructure

import io.getquill.{MysqlJdbcContext, SnakeCase}
import net.yoshinorin.cahsper.models.db.Table
import net.yoshinorin.cahsper.models.request.QueryParamater

sealed abstract class OrderType

object OrderType {

  object ASC extends OrderType
  object DESC extends OrderType

  implicit class OrderConverter(s: String) {

    def toOrder: OrderType = {
      s.toUpperCase match {
        case "ASC" => OrderType.ASC
        case _ => OrderType.DESC
      }
    }
  }

}

object DataBaseContext {

  lazy val ctx = new MysqlJdbcContext[SnakeCase](SnakeCase, "db.ctx")

}

class DataBaseContext[T <: Table] {

  val ctx = DataBaseContext.ctx

  import ctx._
  import net.yoshinorin.cahsper.infrastructure.OrderType.OrderConverter

  def sortByCreatedAt(query: Quoted[Query[T]], order: String)(implicit schemaMeta: SchemaMeta[T]): Quoted[Query[T]] = {
    order.toOrder match {
      case OrderType.ASC =>
        quote {
          query.sortBy(_.createdAt)(Ord.asc)
        }
      case _ =>
        quote {
          query.sortBy(_.createdAt)(Ord.desc)
        }
    }
  }

  def filterWithQueryParam(queryParamater: QueryParamater)(implicit schemaMeta: SchemaMeta[T]): Quoted[Query[T]] = quote {
    query[T]
      .filter(_.createdAt >= lift(queryParamater.from))
      .filter(_.createdAt <= lift(queryParamater.to))
      .drop(lift(queryParamater.page * queryParamater.limit))
      .take(lift(queryParamater.limit))
  }

}
