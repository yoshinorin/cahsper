package net.yoshinorin.cahsper.definitions

import io.circe.Encoder
import io.circe.generic.semiauto._

case class Messages(
  messages: String
)

object Messages {
  implicit val encodeMessage: Encoder[Messages] = deriveEncoder[Messages]
}
