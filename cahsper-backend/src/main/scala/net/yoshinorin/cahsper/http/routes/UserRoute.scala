package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.syntax._
import net.yoshinorin.cahsper.http.auth.Auth
import net.yoshinorin.cahsper.models.Message
import net.yoshinorin.cahsper.services.UserService

class UserRoute(
  auth: Auth,
  userService: UserService
) {

  def route: Route = {
    pathPrefix("users") {
      pathEndOrSingleSlash {
        get {
          onSuccess(userService.getAll) { result =>
            complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
          }
        } ~ {
          post {
            auth.authenticate { user =>
              onSuccess(userService.create(user)) { result =>
                complete(HttpResponse(Created, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
              }
            }
          }
        }
      } ~ {
        // hostName/users/exampleUser
        pathPrefix(".+".r) { userName =>
          get {
            onSuccess(userService.findByName(userName)) {
              case Some(user) =>
                complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${user.asJson}")))
              case _ =>
                complete(HttpResponse(NotFound, entity = HttpEntity(ContentTypes.`application/json`, s"${Message("NotFound").asJson}")))
            }
          }
        }
      }
    }
  }

}
