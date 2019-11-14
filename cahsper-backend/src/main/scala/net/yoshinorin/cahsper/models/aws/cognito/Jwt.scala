package net.yoshinorin.cahsper.models.aws.cognito

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.parser.decode
import net.yoshinorin.cahsper.models.Message
import org.slf4j.LoggerFactory

/**
 * AWS Cognito Jwt Claims case class
 */
case class Jwt(
  sub: String,
  event_id: String,
  token_use: String,
  scope: String,
  auth_time: Int,
  iss: String,
  exp: Int,
  iat: Int,
  jti: String,
  client_id: String,
  username: String
)

object Jwt {

  private val logger = LoggerFactory.getLogger(this.getClass)
  implicit val decodeAwsJwtClaims: Decoder[Jwt] = deriveDecoder[Jwt]

  implicit class convertJwtClaims(string: String) {

    /**
     * Convert string to AwsCognitoJwtClaims case class
     *
     * @return
     */
    def toJwtClaims: Either[Message, Jwt] = {
      decode[Jwt](string) match {
        case Right(jwtClaims) => Right(jwtClaims)
        case Left(error) =>
          logger.error(error.getMessage)
          Left(Message(error.getMessage))
      }
    }
  }

}
