package net.yoshinorin.cahsper.definitions

import io.circe.Encoder
import io.circe.generic.semiauto._

case class Message(
  messages: String
)

object Message {
  implicit val encodeMessage: Encoder[Message] = deriveEncoder[Message]
}