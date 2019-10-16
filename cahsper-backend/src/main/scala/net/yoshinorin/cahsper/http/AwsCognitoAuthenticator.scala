package net.yoshinorin.cahsper.http

import akka.http.scaladsl.model.headers.{HttpChallenge, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives.authenticateOrRejectWithChallenge
import akka.http.scaladsl.server.directives.{AuthenticationDirective, AuthenticationResult}
import net.yoshinorin.cahsper.auth.AwsCognito
import net.yoshinorin.cahsper.models.AwsCognitoJwtClaims
import net.yoshinorin.cahsper.models.AwsCognitoJwtClaims.convertJwtClaims

import scala.concurrent.Future

trait AwsCognitoAuthenticator {

  // TODO: Clean up
  def authenticate: AuthenticationDirective[AwsCognitoJwtClaims] = {

    authenticateOrRejectWithChallenge[OAuth2BearerToken, AwsCognitoJwtClaims] {
      case Some(OAuth2BearerToken(bearerToken)) => {
        AwsCognito.validateJwt(bearerToken) match {
          case Right(jwtClaimsSet) => {
            jwtClaimsSet.toJSONObject.toJSONString.toAwsCognitoJwtClaims match {
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
