package net.yoshinorin.cahsper.domains.users

import io.circe.Encoder
import io.circe.generic.semiauto._

final case class User(name: String)

object User {
  implicit val encodeUserName: Encoder[User] = deriveEncoder[User]
}
