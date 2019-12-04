package net.yoshinorin.cahsper.models.request

import java.time.ZonedDateTime

case class QueryParamater(
  page: Int = 1,
  limit: Int = 100,
  from: Long = 0,
  to: Long = ZonedDateTime.now.plusMinutes(1).toEpochSecond //TODO: improve
)
