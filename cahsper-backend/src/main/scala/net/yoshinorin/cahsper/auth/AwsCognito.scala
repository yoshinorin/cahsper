package net.yoshinorin.cahsper.auth

import java.net.URL

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.{JWKSource, RemoteJWKSet}
import com.nimbusds.jose.proc.{JWSVerificationKeySelector, SecurityContext}
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.proc.{BadJWTException, ConfigurableJWTProcessor, DefaultJWTProcessor}
import net.yoshinorin.cahsper.config.Config
import net.yoshinorin.cahsper.definitions.Messages
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

object AwsCognito {

  val badJwtMessage: Messages = Messages("Invalid JWT.")
  val exceptionOccured: Messages = Messages("Exception occured.")

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
  def validateJwt(jwt: String): Either[Messages, JWTClaimsSet] = {

    Try(jwtProcessor.process(jwt, null)) match {
      case Success(jwtClaimsSet: JWTClaimsSet) =>
        // TODO: Add more verify https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-tokens-verifying-a-jwt.html
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
