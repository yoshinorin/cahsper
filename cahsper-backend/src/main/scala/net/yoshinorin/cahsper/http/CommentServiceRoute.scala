package net.yoshinorin.cahsper.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes._
import io.circe.syntax._
import net.yoshinorin.cahsper.models.request.{CommentRequestFormat, CreateCommentRequestFormat}
import net.yoshinorin.cahsper.services.CommentService

class CommentServiceRoute(commentService: CommentService)(implicit actorSystem: ActorSystem) {

  def route: Route = {
    pathPrefix("comments") {
      pathEndOrSingleSlash {
        get {
          onSuccess(commentService.getAll) { result =>
            complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
          }
        } ~
          post {
            entity(as[String]) { payload =>
              val result = for {
                maybeCreateCommentFormat <- CommentRequestFormat.convertFromJsonString[CreateCommentRequestFormat](payload)
                createCommentRequestFormat <- maybeCreateCommentFormat.validate
              } yield createCommentRequestFormat
              result match {
                case Right(createCommentRequestFormat) =>
                  onSuccess(commentService.create("TODO", createCommentRequestFormat)) { result =>
                    complete(HttpResponse(Created, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
                  }
                case Left(message) =>
                  complete(HttpResponse(BadRequest, entity = HttpEntity(ContentTypes.`application/json`, s"${message.asJson}")))
              }
            }
          }
      }
    }
  }

}
