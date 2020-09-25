package net.yoshinorin.cahsper.domains.users

import java.time.ZonedDateTime

import io.circe.Encoder
import io.circe.generic.semiauto._
import net.yoshinorin.cahsper.models.db.Table

final case class Users(
  name: String, // TODO: Should use UserName case class
  override val createdAt: Long = ZonedDateTime.now.toEpochSecond
) extends Table

object Users {
  implicit val encodeUser: Encoder[Users] = deriveEncoder[Users]
  implicit val encodeUsers: Encoder[List[Users]] = Encoder.encodeList[Users]
}
