package net.yoshinorin.cahsper.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes._
import io.circe.syntax._
import net.yoshinorin.cahsper.services.CommentService

class CommentServiceRoute(commentService: CommentService)(implicit actorSystem: ActorSystem) {

  def route: Route = {
    pathPrefix("comments") {
      pathEndOrSingleSlash {
        get {
          onSuccess(commentService.getAll) { result =>
            complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
          }
        }
      }
    }
  }

}
