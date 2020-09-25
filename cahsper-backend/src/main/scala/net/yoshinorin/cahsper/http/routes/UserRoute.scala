package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.circe.syntax._
import net.yoshinorin.cahsper.domains.users.{UserName, Users}
import net.yoshinorin.cahsper.http.auth.Auth
import net.yoshinorin.cahsper.models.Message
import net.yoshinorin.cahsper.models.request.{CommentRequestFormat, CreateCommentRequestFormat, QueryParamater}
import net.yoshinorin.cahsper.services.{CommentService, UserService}

class UserRoute(
  auth: Auth,
  userService: UserService,
  commentService: CommentService
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
            auth.authenticate { userName =>
              onSuccess(userService.create(UserName(userName.value))) { result =>
                complete(HttpResponse(Created, entity = HttpEntity(ContentTypes.`application/json`, s"${result.asJson}")))
              }
            }
          }
        }
      } ~ {
        // hostName/users/exampleUser
        pathPrefix(".+".r) { userName =>
          pathEndOrSingleSlash {
            get {
              onSuccess(userService.findByName(UserName(userName))) {
                case Some(user) =>
                  complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${user.asJson}")))
                case _ =>
                  complete(HttpResponse(NotFound, entity = HttpEntity(ContentTypes.`application/json`, s"${Message("NotFound").asJson}")))
              }
            }
          } ~
            pathPrefix("comments") {
              get {
                parameters("page".as[Int].?, "limit".as[Int].?, "from".as[Long].?, "to".as[Long].?, "order".as[String].?) { (page, limit, from, to, order) =>
                  onSuccess(commentService.findByUserName(UserName(userName), QueryParamater(page, limit, from, to, order))) { comments =>
                    // TODO: should sort using by SQL
                    complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, s"${comments.asJson}")))
                  }
                }
              } ~
                post {
                  auth.authenticate { userName =>
                    // TODO: check user existence
                    entity(as[String]) { payload =>
                      val result = for {
                        maybeCreateCommentFormat <- CommentRequestFormat.convertFromJsonString[CreateCommentRequestFormat](payload)
                        createCommentRequestFormat <- maybeCreateCommentFormat.validate
                      } yield createCommentRequestFormat
                      result match {
                        case Right(createCommentRequestFormat) =>
                          onSuccess(commentService.create(Users(userName.value), createCommentRequestFormat)) { result =>
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
    }
  }

}
