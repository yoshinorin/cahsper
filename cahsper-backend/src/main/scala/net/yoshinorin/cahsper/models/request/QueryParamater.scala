package net.yoshinorin.cahsper.models.request

import java.time.ZonedDateTime

case class QueryParamater(
  page: Int = 0,
  limit: Int = 100,
  from: Long = 0,
  to: Long = ZonedDateTime.now.plusMinutes(1).toEpochSecond //TODO: improve
)

object QueryParamater {

  def apply(
    page: Option[Int],
    limit: Option[Int],
    from: Option[Long],
    to: Option[Long]
  ): QueryParamater = {
    new QueryParamater(
      page.getOrElse(1) - 1,
      if (limit.getOrElse(100) > 100) 100 else limit.getOrElse(100),
      from.getOrElse(0),
      to.getOrElse(ZonedDateTime.now.plusMinutes(1).toEpochSecond)
    )
  }

}
