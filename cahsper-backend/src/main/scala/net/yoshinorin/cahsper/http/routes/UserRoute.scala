package net.yoshinorin.cahsper.http.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.syntax._
import net.yoshinorin.cahsper.http.AwsCognitoAuthenticator
import net.yoshinorin.cahsper.services.UserService

class UserRoute(userService: UserService)(implicit actorSystem: ActorSystem) extends AwsCognitoAuthenticator {

  def route: Route = {
    pathPrefix("users") {
      pathEndOrSingleSlash {
        getUserRoute ~
          authenticate { jwtClaim =>
            postUserRoute(jwtClaim.username)
          }
      }
    }
  }

  // $COVERAGE-OFF$
  // NOTE: This route for unit test. Never use at production.
  def nonAuthRoute: Route = {
    pathPrefix("users") {
      getUserRoute ~
        pathEndOrSingleSlash {
          postUserRoute("JohnDoe")
        }
    }
  }
  // $COVERAGE-ON$

  private[this] def getUserRoute: Route = {
    get {
      onSuccess(userService.getAll) { result =>
        complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
      }
    }
  }

  private[this] def postUserRoute(userName: String): Route = {
    post {
      onSuccess(userService.create(userName)) { result =>
        complete(HttpResponse(Created, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
      }
    }
  }

}
