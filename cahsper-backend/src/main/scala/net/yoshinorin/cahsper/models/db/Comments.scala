package net.yoshinorin.cahsper.models.db

import java.time.ZonedDateTime
import io.circe.generic.semiauto._
import io.circe.Encoder

case class Comments(
  id: Int = 0,
  userName: String,
  comment: String,
  createdAt: Long = ZonedDateTime.now.toEpochSecond
)

object Comments {
  implicit val encodeComment: Encoder[Comments] = deriveEncoder[Comments]
  implicit val encodeComments: Encoder[List[Comments]] = Encoder.encodeList[Comments]
}
