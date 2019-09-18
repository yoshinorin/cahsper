package net.yoshinorin.cahsper.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes._
import io.circe.syntax._
import net.yoshinorin.cahsper.services.UserService

class UserRoute(userService: UserService)(implicit actorSystem: ActorSystem) {

  def route: Route = {
    pathPrefix("users") {
      pathEndOrSingleSlash {
        get {
          //TODO: return all user
          complete(HttpResponse(NotFound, entity = HttpEntity(ContentTypes.`application/json`, "{\"message\":\"Not found\"}")))
        } ~ post {
          //TODO: Create user from AWS Cognito claim
          entity(as[String]) { userName =>
            onSuccess(userService.create(userName)) { result =>
              complete(HttpResponse(Created, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
            }
          }
        }
      }
    }
  }

}
