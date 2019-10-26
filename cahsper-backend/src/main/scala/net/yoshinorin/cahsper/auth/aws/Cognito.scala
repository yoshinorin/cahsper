package net.yoshinorin.cahsper.auth.aws

import java.net.URL

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.{JWKSource, RemoteJWKSet}
import com.nimbusds.jose.proc.{JWSVerificationKeySelector, SecurityContext}
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.proc.{BadJWTException, ConfigurableJWTProcessor, DefaultJWTProcessor}
import net.yoshinorin.cahsper.config.Config
import net.yoshinorin.cahsper.definitions.{Message, Jwt}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

// TODO: refactor architecture
object Cognito {

  val badJwtMessage: Message = Message("Invalid JWT.")
  val exceptionOccured: Message = Message("Exception occured.")

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val awsCognitoJwk = Config.awsCognitoJwkUrl
  private val jwkSet: JWKSource[SecurityContext] = new RemoteJWKSet(new URL(awsCognitoJwk))

  private val jwtProcessor: ConfigurableJWTProcessor[SecurityContext] = new DefaultJWTProcessor[SecurityContext]
  private val jWSVerificationKeySelector: JWSVerificationKeySelector[SecurityContext] = new JWSVerificationKeySelector(JWSAlgorithm.RS256, jwkSet)

  jwtProcessor.setJWSKeySelector(jWSVerificationKeySelector)

  /**
   * Validate jwt is valid or invalid
   *
   * @param jwt
   * @return
   */
  def validateJwt(jwt: Jwt): Either[Message, JWTClaimsSet] = {

    Try(jwtProcessor.process(jwt.token, null)) match {
      case Success(jwtClaimsSet: JWTClaimsSet) =>
        // TODO: clean up
        if (jwtClaimsSet.getStringClaim("client_id") != Config.awsCognitoAppClientId) {
          logger.error("jwt client_id is wrong.")
          return Left(badJwtMessage)
        }

        if (jwtClaimsSet.getIssuer.split("/").last != Config.awsCognitoJwkIss) {
          logger.error("jwt iss is wrong.")
          return Left(badJwtMessage)
        }

        if (jwtClaimsSet.getStringClaim("token_use") != "access") {
          logger.error("jwt token_use must be 'access'.")
          return Left(badJwtMessage)
        }

        Right(jwtClaimsSet)
      case Failure(badJwtException: BadJWTException) =>
        logger.error(badJwtException.toString)
        Left(badJwtMessage)
      case Failure(exception: Exception) =>
        logger.error(exception.toString)
        Left(exceptionOccured)
    }

  }
}
