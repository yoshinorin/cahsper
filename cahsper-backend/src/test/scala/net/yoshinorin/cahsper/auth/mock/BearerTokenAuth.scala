package net.yoshinorin.cahsper.auth.mock

import akka.http.scaladsl.model.headers.{HttpChallenge, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives.authenticateOrRejectWithChallenge
import akka.http.scaladsl.server.directives.{AuthenticationDirective, AuthenticationResult}
import net.yoshinorin.cahsper.domains.models.users.UserName
import net.yoshinorin.cahsper.http.auth.Auth

import scala.concurrent.Future

class BearerTokenAuth(userName: String) extends Auth {

  override def authenticate: AuthenticationDirective[UserName] = {
    authenticateOrRejectWithChallenge[OAuth2BearerToken, UserName] {
      case Some(OAuth2BearerToken(bearerToken)) => {
        Future.successful(AuthenticationResult.success(UserName(userName)))
      }
      case _ =>
        Future.successful(AuthenticationResult.failWithChallenge(HttpChallenge("Bearer", None, Map("error" -> "Requires authentication"))))
    }
  }

}
