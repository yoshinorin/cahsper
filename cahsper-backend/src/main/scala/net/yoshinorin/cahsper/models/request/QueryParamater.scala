package net.yoshinorin.cahsper.models.request

final case class QueryParamater(
  page: Int = 0,
  limit: Int = 100,
  from: Long = 0,
  to: Long = Long.MaxValue,
  order: String = "desc"
)

object QueryParamater {

  def apply(
    page: Option[Int],
    limit: Option[Int],
    from: Option[Long],
    to: Option[Long],
    order: Option[String]
  ): QueryParamater = {
    new QueryParamater(
      page.getOrElse(1) - 1,
      if (limit.getOrElse(100) > 100) 100 else limit.getOrElse(100),
      from.getOrElse(0),
      to.getOrElse(Long.MaxValue),
      order.getOrElse("desc")
    )
  }

}
