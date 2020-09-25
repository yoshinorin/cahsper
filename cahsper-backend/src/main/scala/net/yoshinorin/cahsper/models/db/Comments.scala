package net.yoshinorin.cahsper.models.db

import java.time.ZonedDateTime
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

final case class Comments(
  id: Int = 0,
  userName: String, // TODO: Should use UserName case class
  comment: String,
  override val createdAt: Long = ZonedDateTime.now.toEpochSecond
) extends Table

object Comments {
  implicit val encodeComment: Encoder[Comments] = deriveEncoder[Comments]
  implicit val encodeComments: Encoder[List[Comments]] = Encoder.encodeList[Comments]
}
