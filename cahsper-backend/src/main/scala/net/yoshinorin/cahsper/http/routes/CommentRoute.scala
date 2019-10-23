package net.yoshinorin.cahsper.http.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import io.circe.syntax._
import net.yoshinorin.cahsper.http.AwsCognitoAuthenticator
import net.yoshinorin.cahsper.models.request.{CommentRequestFormat, CreateCommentRequestFormat}
import net.yoshinorin.cahsper.services.CommentService

class CommentRoute(commentService: CommentService)(implicit actorSystem: ActorSystem) extends AwsCognitoAuthenticator {

  def route: Route = {
    pathPrefix("comments") {
      pathEndOrSingleSlash {
        read ~
          authenticate { jwtClaim =>
            write(jwtClaim.username)
          }
      }
    }
  }

  // $COVERAGE-OFF$
  // NOTE: This route for unit test. Never use at production.
  def devRoute: Route = {
    pathPrefix("comments") {
      pathEndOrSingleSlash {
        read ~
          write("JohnDoe")
      }
    }
  }
  // $COVERAGE-ON$

  private[this] def read: Route = {
    get {
      onSuccess(commentService.getAll) { result =>
        complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
      }
    }
  }

  private[this] def write(userName: String): Route = {
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
