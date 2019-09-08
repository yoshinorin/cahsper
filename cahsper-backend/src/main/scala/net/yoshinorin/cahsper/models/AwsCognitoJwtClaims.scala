package net.yoshinorin.cahsper.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.parser.decode
import net.yoshinorin.cahsper.definitions.Messages
import org.slf4j.LoggerFactory

/**
 * AWS Cognito Jwt Claims case class
 */
case class AwsCognitoJwtClaims(
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

object AwsCognitoJwtClaims {

  private val logger = LoggerFactory.getLogger(this.getClass)
  implicit val decodeAwsJwtClaims: Decoder[AwsCognitoJwtClaims] = deriveDecoder[AwsCognitoJwtClaims]

  implicit class convertJwtClaims(string: String) {

    /**
     * Convert string to AwsCognitoJwtClaims case class
     *
     * @return
     */
    def toAwsCognitoJwtClaims: Either[Messages, AwsCognitoJwtClaims] = {
      decode[AwsCognitoJwtClaims](string) match {
        case Right(jwtClaims) => Right(jwtClaims)
        case Left(error) =>
          logger.error(error.getMessage)
          Left(Messages(error.getMessage))
      }
    }
  }

}
