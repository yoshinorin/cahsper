package net.yoshinorin.cahsper.domains.models.users

import io.circe.Encoder
import io.circe.generic.semiauto._

final case class UserName(value: String)

object UserName {
  implicit val encodeUserName: Encoder[UserName] = deriveEncoder[UserName]
}
