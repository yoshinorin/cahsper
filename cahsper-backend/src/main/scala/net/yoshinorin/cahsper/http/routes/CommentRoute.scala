package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.syntax._
import net.yoshinorin.cahsper.http.auth.Auth
import net.yoshinorin.cahsper.models.request.QueryParamater
import net.yoshinorin.cahsper.services.CommentService

class CommentRoute(
  auth: Auth,
  commentService: CommentService
) {

  def route: Route = {
    pathPrefix("comments") {
      pathEndOrSingleSlash {
        get {
          parameters("page".as[Int].?, "limit".as[Int].?, "from".as[Long].?, "to".as[Long].?) { (page, limit, from, to) =>
            onSuccess(commentService.getAll(QueryParamater(page, limit, from, to))) { result =>
              complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${result.reverse.asJson}")))
            }
          }
        }
      }
    }
  }

}
