package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.yoshinorin.cahsper.auth.mock.BearerTokenAuth
import net.yoshinorin.cahsper.http
import net.yoshinorin.cahsper.models.db.Comments
import net.yoshinorin.cahsper.models.request.QueryParamater
import net.yoshinorin.cahsper.services.CommentService
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Future

// testOnly *CommentRouteSpec
class CommentRouteSpec extends AnyWordSpec with ScalatestRouteTest {

  val mockCommentService: CommentService = Mockito.mock(classOf[CommentService])

  when(mockCommentService.getAll(QueryParamater()))
    .thenReturn(
      Future(
        Seq(
          Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
          Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
        )
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
        |    "id" : 1,
        |    "userName" : "YoshinoriN",
        |    "comment" : "This is a test one.",
        |    "createdAt" : 1567814290
        |  },
        |  {
        |    "id" : 2,
        |    "userName" : "YoshinoriN",
        |    "comment" : "This is a test two.",
        |    "createdAt" : 1567814391
        |  }
        |]
      """.stripMargin.replaceAll("\n", "").replaceAll(" ", "")

      Get("/comments/") ~> commentServiceRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].replaceAll("\n", "").replaceAll(" ", "") == expectJson)
      }

    }

  }

}
