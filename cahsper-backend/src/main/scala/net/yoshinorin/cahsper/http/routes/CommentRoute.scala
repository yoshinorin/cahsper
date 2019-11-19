package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import io.circe.syntax._
import net.yoshinorin.cahsper.http.auth.Auth
import net.yoshinorin.cahsper.models.request.{CommentRequestFormat, CreateCommentRequestFormat}
import net.yoshinorin.cahsper.services.CommentService

class CommentRoute(
  auth: Auth,
  commentService: CommentService
) {

  def route: Route = {
    pathPrefix("comments") {
      pathEndOrSingleSlash {
        read ~ write
      }
    }
  }

  private[this] def read: Route = {
    get {
      onSuccess(commentService.getAll) { result =>
        complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.reverse.asJson}")))
      }
    }
  }

  private[this] def write: Route = {
    post {
      auth.authenticate { user =>
        entity(as[String]) { payload =>
          val result = for {
            maybeCreateCommentFormat <- CommentRequestFormat.convertFromJsonString[CreateCommentRequestFormat](payload)
            createCommentRequestFormat <- maybeCreateCommentFormat.validate
          } yield createCommentRequestFormat
          result match {
            case Right(createCommentRequestFormat) =>
              onSuccess(commentService.create(user, createCommentRequestFormat)) { result =>
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
