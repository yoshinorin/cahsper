package net.yoshinorin.cahsper.http.auth

import akka.http.scaladsl.model.headers.{HttpChallenge, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives.authenticateOrRejectWithChallenge
import akka.http.scaladsl.server.directives.{AuthenticationDirective, AuthenticationResult}
import net.yoshinorin.cahsper.auth.aws.Cognito
import net.yoshinorin.cahsper.definitions.User
import net.yoshinorin.cahsper.models.aws.cognito.Jwt
import net.yoshinorin.cahsper.models.aws.cognito.Jwt.convertJwtClaims

import scala.concurrent.Future

trait Cognito {

  // TODO: Clean up
  def authenticate: AuthenticationDirective[User] = {

    authenticateOrRejectWithChallenge[OAuth2BearerToken, User] {
      case Some(OAuth2BearerToken(bearerToken)) => {
        Cognito.validateJwt(bearerToken) match {
          case Right(jwtClaimsSet) => {
            jwtClaimsSet.toJSONObject.toJSONString.toJwtClaims match {
              case Right(jwtClaims) => Future.successful(AuthenticationResult.success(User(jwtClaims.username)))
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
