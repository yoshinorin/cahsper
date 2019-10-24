package net.yoshinorin.cahsper.http

import akka.http.scaladsl.model.headers.{HttpChallenge, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives.authenticateOrRejectWithChallenge
import akka.http.scaladsl.server.directives.{AuthenticationDirective, AuthenticationResult}
import net.yoshinorin.cahsper.auth.aws.Cognito
import net.yoshinorin.cahsper.models.aws.cognito.Jwt.convertJwtClaims
import net.yoshinorin.cahsper.models.aws.cognito.Jwt

import scala.concurrent.Future

trait AwsCognitoAuthenticator {

  // TODO: Clean up
  def authenticate: AuthenticationDirective[Jwt] = {

    authenticateOrRejectWithChallenge[OAuth2BearerToken, Jwt] {
      case Some(OAuth2BearerToken(bearerToken)) => {
        Cognito.validateJwt(bearerToken) match {
          case Right(jwtClaimsSet) => {
            jwtClaimsSet.toJSONObject.toJSONString.toJwtClaims match {
              case Right(jwtClaims) => Future.successful(AuthenticationResult.success(jwtClaims))
              case Left(_) =>
                Future.successful(AuthenticationResult.failWithChallenge(HttpChallenge("Bearer", None, Map("error" -> "Internal server error"))))
            }
          }
          case Left(_) =>
            Future.successful(AuthenticationResult.failWithChallenge(HttpChallenge("Bearer", None, Map("error" -> "Invalid token"))))
        }
      }
      case _ =>
        Future.successful(AuthenticationResult.failWithChallenge(HttpChallenge("Bearer", None, Map("error" -> "Requires authentication"))))
    }

  }

}
