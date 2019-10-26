package net.yoshinorin.cahsper.http.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.AuthenticationFailedRejection
import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import net.yoshinorin.cahsper.auth.FakeAuth
import net.yoshinorin.cahsper.definitions.User
import net.yoshinorin.cahsper.http
import net.yoshinorin.cahsper.models.db.Users
import net.yoshinorin.cahsper.services.UserService
import org.mockito.Mockito.when
import org.scalatest.WordSpec
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.{ExecutionContextExecutor, Future}

// testOnly *UserRouteSpec
class UserRouteSpec extends WordSpec with MockitoSugar with ScalatestRouteTest {

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val mockUserService: UserService = mock[UserService]

  when(mockUserService.create(User("JohnDoe")))
    .thenReturn(Future(Users("JohnDoe")))

  when(mockUserService.getAll)
    .thenReturn(
      Future(
        Seq(
          Users("YoshinoriN", 1567814290),
          Users("NoshinoriN", 1567814391)
        )
      )
    )

  val auth = new http.auth.Cognito()
  val userRoute: UserRoute = new UserRoute(auth, mockUserService)

  val fakeAuth = new FakeAuth("JohnDoe")
  val userRouteWithFakeAuth: UserRoute = new UserRoute(fakeAuth, mockUserService)

  "UserRoute" should {

    "return created user" in {
      Post("/users") ~> addCredentials(OAuth2BearerToken("Valid Token")) ~> userRouteWithFakeAuth.route ~> check {
        assert(status == StatusCodes.Created)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].contains("JohnDoe")) //TODO: brush up
      }
    }

    "unauthorized when access token is invalid" in {
      Post("/users") ~> addCredentials(OAuth2BearerToken("Invalid Token")) ~> userRoute.route ~> check {
        // TODO: workaround
        assert(rejections.last.asInstanceOf[AuthenticationFailedRejection].cause == CredentialsRejected)
      }
    }

    "reject when Authorization header is nothing" in {
      // TODO: workaround
      Post("/users") ~> userRoute.route ~> check {
        assert(rejections.last.asInstanceOf[AuthenticationFailedRejection].cause == CredentialsMissing)
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

    }

  }

}
