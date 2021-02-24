package net.yoshinorin.cahsper.domains.models.comments

import java.time.ZonedDateTime
import java.util.UUID

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import net.yoshinorin.cahsper.models.db.Table

final case class Comments(
  id: String = UUID.randomUUID().toString,
  userName: String,
  comment: String,
  override val createdAt: Long = ZonedDateTime.now.toEpochSecond
) extends Table

object Comments {
  implicit val encodeComment: Encoder[Comments] = deriveEncoder[Comments]
  implicit val encodeComments: Encoder[List[Comments]] = Encoder.encodeList[Comments]
}
