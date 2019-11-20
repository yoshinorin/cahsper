package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.syntax._
import net.yoshinorin.cahsper.http.auth.Auth
import net.yoshinorin.cahsper.services.CommentService

class CommentRoute(
  auth: Auth,
  commentService: CommentService
) {

  def route: Route = {
    pathPrefix("comments") {
      pathEndOrSingleSlash {
        get {
          onSuccess(commentService.getAll) { result =>
            complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.reverse.asJson}")))
          }
        }
      }
    }
  }

}
