package net.yoshinorin.cahsper.models.db

import java.time.ZonedDateTime

import io.circe.Encoder
import io.circe.generic.semiauto._

case class Users(
  name: String,
  override val createdAt: Long = ZonedDateTime.now.toEpochSecond
) extends Table

object Users {
  implicit val encodeUser: Encoder[Users] = deriveEncoder[Users]
  implicit val encodeUsers: Encoder[List[Users]] = Encoder.encodeList[Users]
}
