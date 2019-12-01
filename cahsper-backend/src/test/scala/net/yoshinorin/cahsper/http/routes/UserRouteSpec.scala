package net.yoshinorin.cahsper.http.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.AuthenticationFailedRejection
import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.yoshinorin.cahsper.auth.mock.BearerTokenAuth
import net.yoshinorin.cahsper.http
import net.yoshinorin.cahsper.models.User
import net.yoshinorin.cahsper.models.db.{Comments, Users}
import net.yoshinorin.cahsper.models.request.CreateCommentRequestFormat
import net.yoshinorin.cahsper.services.{CommentService, UserService}
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.{ExecutionContextExecutor, Future}

// testOnly *UserRouteSpec
class UserRouteSpec extends AnyWordSpec with ScalatestRouteTest {

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val mockUserService: UserService = Mockito.mock(classOf[UserService])

  when(mockUserService.create(User("YoshinoriN")))
    .thenReturn(Future(Users("YoshinoriN")))

  when(mockUserService.getAll)
    .thenReturn(
      Future(
        Seq(
          Users("YoshinoriN", 1567814290),
          Users("NoshinoriN", 1567814391)
        )
      )
    )

  when(mockUserService.findByName("YoshinoriN"))
    .thenReturn(Future(Option(Users("YoshinoriN", 1567814290))))

  when(mockUserService.findByName("exampleUser"))
    .thenReturn(Future(None))

  val mockCommentService: CommentService = Mockito.mock(classOf[CommentService])

  when(mockCommentService.findById(1))
    .thenReturn(Future(Some(Comments(1, "YoshinoriN", "This is a test one.", 1567814290))))

  when(mockCommentService.create(User("YoshinoriN"), CreateCommentRequestFormat("Hello")))
    .thenReturn(
      Future(
        Comments(3, "YoshinoriN", "Hello", 1567814391)
      )
    )

  when(mockCommentService.findByUserName(User("YoshinoriN")))
    .thenReturn(
      Future(
        Seq(
          Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
          Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
        )
      )
    )

  when(mockCommentService.findByUserName(User("JhonDue")))
    .thenReturn(Future(Seq.empty))

  val auth = new http.auth.Cognito()
  val userRoute: UserRoute = new UserRoute(auth, mockUserService, mockCommentService)

  val fakeAuth = new BearerTokenAuth("YoshinoriN")
  val userRouteWithFakeAuth: UserRoute = new UserRoute(fakeAuth, mockUserService, mockCommentService)

  "UserRoute" should {

    "return created user" in {
      Post("/users") ~> addCredentials(OAuth2BearerToken("Valid Token")) ~> userRouteWithFakeAuth.route ~> check {
        assert(status == StatusCodes.Created)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].contains("YoshinoriN")) //TODO: brush up
      }
    }

    "unauthorized when access token is invalid" in {
      Post("/users") ~> addCredentials(OAuth2BearerToken("Invalid Token")) ~> userRoute.route ~> check {
        assert(rejection.asInstanceOf[AuthenticationFailedRejection].cause == CredentialsRejected)
      }
    }

    "reject when Authorization header is nothing" in {
      Post("/users") ~> userRoute.route ~> check {
        assert(rejection.asInstanceOf[AuthenticationFailedRejection].cause == CredentialsMissing)
      }
    }

    "return all users JSON" in {

      val expectJson =
        """
          |[
          |  {
          |    "name" : "YoshinoriN",
          |    "createdAt" : 1567814290
          |  },
          |  {
          |    "name" : "NoshinoriN",
          |    "createdAt" : 1567814391
          |  }
          |]
      """.stripMargin.replaceAll("\n", "").replaceAll(" ", "")

      Get("/users/") ~> userRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].replaceAll("\n", "").replaceAll(" ", "") == expectJson)
      }

      Get("/users") ~> userRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].replaceAll("\n", "").replaceAll(" ", "") == expectJson)
      }

    }

    "return specify user JSON" in {

      val expectJson =
        """
          |{
          |  "name" : "YoshinoriN",
          |  "createdAt" : 1567814290
          |}
      """.stripMargin.replaceAll("\n", "").replaceAll(" ", "")

      Get("/users/YoshinoriN") ~> userRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].replaceAll("\n", "").replaceAll(" ", "") == expectJson)
      }

    }

    "user not found" in {

      Get("/users/exampleUser") ~> userRoute.route ~> check {
        assert(status == StatusCodes.NotFound)
        assert(contentType == ContentTypes.`application/json`)
      }

    }

    "unauthorized when post comment that access token is invalid" in {
      Post("/users/JohnDue/comments/") ~> addCredentials(OAuth2BearerToken("Invalid Token")) ~> userRoute.route ~> check {
        assert(rejection.asInstanceOf[AuthenticationFailedRejection].cause == CredentialsRejected)
      }
    }

    "reject post comment when Authorization header is nothing" in {
      Post("/users/JohnDue/comments/") ~> userRoute.route ~> check {
        assert(rejection.asInstanceOf[AuthenticationFailedRejection].cause == CredentialsMissing)
      }
    }

    "return 404 when token claim user to directive user name does not unmatch" in {
      Post("/users/JohnDue/comments/")
        .withEntity(ContentTypes.`application/json`, """{"comment":"Hello"}""") ~> addCredentials(OAuth2BearerToken("Valid Token")) ~> userRouteWithFakeAuth.route ~> check {
        assert(status == StatusCodes.NotFound)
        assert(contentType == ContentTypes.`application/json`)
      }
    }

    "return 400 when post comment's payload is wrong format" in {
      val json = """{Not a JSON}""".stripMargin
      Post("/users/YoshinoriN/comments/")
        .withEntity(ContentTypes.`application/json`, json) ~> addCredentials(OAuth2BearerToken("Valid Token")) ~> userRouteWithFakeAuth.route ~> check {
        assert(status == StatusCodes.BadRequest)
        assert(contentType == ContentTypes.`application/json`)
      }
    }

    "get comment by user" in {
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

      Get("/users/YoshinoriN/comments/") ~> userRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].replaceAll("\n", "").replaceAll(" ", "") == expectJson)
      }

      Get("/users/JhonDue/comments/") ~> userRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].replaceAll("\n", "") == "[]")
      }
    }

    "create a new comment" in {
      Post("/users/YoshinoriN/comments/")
        .withEntity(ContentTypes.`application/json`, """{"comment":"Hello"}""") ~> addCredentials(OAuth2BearerToken("Valid Token")) ~> userRouteWithFakeAuth.route ~> check {
        assert(status == StatusCodes.Created)
        assert(contentType == ContentTypes.`application/json`)
      }
    }

  }

}
