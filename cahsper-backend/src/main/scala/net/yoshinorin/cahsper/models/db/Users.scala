package net.yoshinorin.cahsper.models.db

import java.time.ZonedDateTime

import io.circe.Encoder
import io.circe.generic.semiauto._

case class Users(
  name: String,
  createdAt: Long = ZonedDateTime.now.toEpochSecond
)

object Users {
  implicit val encodeUser: Encoder[Users] = deriveEncoder[Users]
  implicit val encodeUsers: Encoder[List[Users]] = Encoder.encodeList[Users]
}
