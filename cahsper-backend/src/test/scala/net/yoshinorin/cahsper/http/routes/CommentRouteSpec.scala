package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.AuthenticationFailedRejection
import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.yoshinorin.cahsper.auth.mock.BearerTokenAuth
import net.yoshinorin.cahsper.http
import net.yoshinorin.cahsper.models.User
import net.yoshinorin.cahsper.models.db.Comments
import net.yoshinorin.cahsper.models.request.CreateCommentRequestFormat
import net.yoshinorin.cahsper.services.CommentService
import org.mockito.Mockito._
import org.scalatest.WordSpec
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Future

// testOnly *CommentRouteSpec
class CommentRouteSpec extends WordSpec with MockitoSugar with ScalatestRouteTest {

  val mockCommentService: CommentService = mock[CommentService]

  when(mockCommentService.findById(1))
    .thenReturn(Future(Some(Comments(1, "YoshinoriN", "This is a test one.", 1567814290))))

  when(mockCommentService.getAll)
    .thenReturn(
      Future(
        Seq(
          Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
          Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
        )
      )
    )

  when(mockCommentService.create(User("JohnDue"), CreateCommentRequestFormat("Hello")))
    .thenReturn(
      Future(
        Comments(3, "JohnDue", "Hello", 1567814391)
      )
    )

  val auth = new http.auth.Cognito()
  val commentServiceRoute: CommentRoute = new CommentRoute(auth, mockCommentService)

  val fakeAuth = new BearerTokenAuth("JohnDue")
  val commentServiceRouteWithFakeAuth: CommentRoute = new CommentRoute(fakeAuth, mockCommentService)

  "CommentServiceRoute" should {

    "return all comments JSON" in {

      val expectJson =
        """
        |[
        |  {
        |    "id" : 2,
        |    "userName" : "YoshinoriN",
        |    "comment" : "This is a test two.",
        |    "createdAt" : 1567814391
        |  },
        |  {
        |    "id" : 1,
        |    "userName" : "YoshinoriN",
        |    "comment" : "This is a test one.",
        |    "createdAt" : 1567814290
        |  }
        |]
      """.stripMargin.replaceAll("\n", "").replaceAll(" ", "")

      Get("/comments/") ~> commentServiceRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].replaceAll("\n", "").replaceAll(" ", "") == expectJson)
      }

    }

    "unauthorized when access token is invalid" in {
      Post("/comments/") ~> addCredentials(OAuth2BearerToken("Invalid Token")) ~> commentServiceRoute.route ~> check {
        assert(rejection.asInstanceOf[AuthenticationFailedRejection].cause == CredentialsRejected)
      }
    }

    "reject when Authorization header is nothing" in {
      Post("/comments/") ~> commentServiceRoute.route ~> check {
        assert(rejection.asInstanceOf[AuthenticationFailedRejection].cause == CredentialsMissing)
      }
    }

    "return 400 when payload is wrong format" in {
      val json = """{Not a JSON}""".stripMargin
      Post("/comments/")
        .withEntity(ContentTypes.`application/json`, json) ~> addCredentials(OAuth2BearerToken("Valid Token")) ~> commentServiceRouteWithFakeAuth.route ~> check {
        assert(status == StatusCodes.BadRequest)
        assert(contentType == ContentTypes.`application/json`)
      }
    }

    "create a new comment" in {
      Post("/comments/")
        .withEntity(ContentTypes.`application/json`, """{"comment":"Hello"}""") ~> addCredentials(OAuth2BearerToken("Valid Token")) ~> commentServiceRouteWithFakeAuth.route ~> check {
        assert(status == StatusCodes.Created)
        assert(contentType == ContentTypes.`application/json`)
      }
    }

  }

}