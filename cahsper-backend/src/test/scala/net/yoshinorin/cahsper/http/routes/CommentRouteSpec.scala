package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.yoshinorin.cahsper.models.db.{CommentRepository, Comments}
import net.yoshinorin.cahsper.services.CommentService
import org.mockito.Mockito._
import org.scalatest.WordSpec
import org.scalatestplus.mockito.MockitoSugar

// testOnly *CommentServiceRouteSpec
class CommentRouteSpec extends WordSpec with MockitoSugar with ScalatestRouteTest {

  val mockCommentRepository: CommentRepository = mock[CommentRepository]

  when(mockCommentRepository.findById(1))
    .thenReturn(Some(Comments(1, "YoshinoriN", "This is a test one.", 1567814290)))

  when(mockCommentRepository.getAll)
    .thenReturn(
      Seq(
        Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
        Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
      )
    )

  when(mockCommentRepository.insert(Comments(3, "YoshinoriN", "This is a test three.")))
    .thenReturn(3)

  val commentService: CommentService = new CommentService(mockCommentRepository)
  val commentServiceRoute: CommentRoute = new CommentRoute(commentService)

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

    "return 400 when payload is wrong format" in {
      val json = """{Not a JSON}""".stripMargin
      Post("/comments/").withEntity(ContentTypes.`application/json`, json) ~> commentServiceRoute.route ~> check {
        assert(status == StatusCodes.BadRequest)
        assert(contentType == ContentTypes.`application/json`)
      }
    }

  }

}
