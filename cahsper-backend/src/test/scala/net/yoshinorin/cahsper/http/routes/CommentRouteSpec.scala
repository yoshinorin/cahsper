package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.yoshinorin.cahsper.auth.mock.BearerTokenAuth
import net.yoshinorin.cahsper.domains.models.comments.Comments
import net.yoshinorin.cahsper.domains.services.CommentService
import net.yoshinorin.cahsper.http
import net.yoshinorin.cahsper.models.request.QueryParamater
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Future

// testOnly net.yoshinorin.cahsper.http.routes.CommentRouteSpec
class CommentRouteSpec extends AnyWordSpec with ScalatestRouteTest {

  val mockCommentService: CommentService = Mockito.mock(classOf[CommentService])

  when(mockCommentService.getAll(QueryParamater()))
    .thenReturn(
      Future(
        Seq(
          Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "yoshinorin", "This is a test one.", 1567814290),
          Comments("cc82737d-769d-11eb-a81e-663f66aa018c", "yoshinorin", "This is a test two.", 1567814391)
        )
      )
    )

  val auth = new http.auth.Cognito()
  val commentServiceRoute: CommentRoute = new CommentRoute(mockCommentService)

  val fakeAuth = new BearerTokenAuth("JohnDue")
  val commentServiceRouteWithFakeAuth: CommentRoute = new CommentRoute(mockCommentService)

  "CommentServiceRoute" should {

    "return all comments JSON" in {

      val expectJson =
        """
        |[
        |  {
        |    "id" : "04b89f7a-6d0c-46c5-87a2-2a35307765bf",
        |    "userName" : "yoshinorin",
        |    "comment" : "This is a test one.",
        |    "createdAt" : 1567814290
        |  },
        |  {
        |    "id" : "cc82737d-769d-11eb-a81e-663f66aa018c",
        |    "userName" : "yoshinorin",
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
