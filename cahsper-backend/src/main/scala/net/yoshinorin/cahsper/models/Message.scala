package net.yoshinorin.cahsper.models

import io.circe.Encoder
import io.circe.generic.semiauto._

final case class Message(
  messages: String
)

object Message {
  implicit val encodeMessage: Encoder[Message] = deriveEncoder[Message]
}
