package net.yoshinorin.cahsper.models.db

import java.time.ZonedDateTime

case class Comments(
  id: Int = 0,
  user: String,
  comment: String,
  createdAt: Long = ZonedDateTime.now.toEpochSecond
)
