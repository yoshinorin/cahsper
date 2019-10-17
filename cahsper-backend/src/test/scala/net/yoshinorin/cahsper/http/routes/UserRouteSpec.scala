package net.yoshinorin.cahsper.http.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.AuthenticationFailedRejection
import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
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

  when(mockUserService.create("JohnDoe"))
    .thenReturn(Future(Users("JohnDoe")))

  val userRoute: UserRoute = new UserRoute(mockUserService)

  "UserRoute" should {

    "return created user" in {
      Post("/users") ~> userRoute.nonAuthRoute ~> check {
        assert(status == StatusCodes.Created)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].contains("JohnDoe")) //TODO: brush up
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

  }

}
