package net.yoshinorin.cahsper.http.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.syntax._
import net.yoshinorin.cahsper.definitions.User
import net.yoshinorin.cahsper.http.auth.Auth
import net.yoshinorin.cahsper.services.UserService

class UserRoute(
  auth: Auth,
  userService: UserService
)(implicit actorSystem: ActorSystem) {

  def route: Route = {
    pathPrefix("users") {
      pathEndOrSingleSlash {
        read ~
          auth.authenticate { user =>
            write(user)
          }
      }
    }
  }

  // $COVERAGE-OFF$
  // NOTE: This route for unit test. Never use at production.
  def devRoute: Route = {
    pathPrefix("users") {
      pathEndOrSingleSlash {
        read ~
          write(User("JohnDoe"))
      }
    }
  }
  // $COVERAGE-ON$

  private[this] def read: Route = {
    get {
      onSuccess(userService.getAll) { result =>
        complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
      }
    }
  }

  private[this] def write(user: User): Route = {
    post {
      onSuccess(userService.create(user)) { result =>
        complete(HttpResponse(Created, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
      }
    }
  }

}
