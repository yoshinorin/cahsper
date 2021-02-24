package net.yoshinorin.cahsper.domains.services

import java.util.UUID

import akka.actor.ActorSystem
import net.yoshinorin.cahsper.application.comments.{CommentCreator, CommentFinder}
import net.yoshinorin.cahsper.domains.models.comments.{CommentRepository, Comments, CreateCommentRequestFormat}
import net.yoshinorin.cahsper.domains.models.users.{UserName, Users}
import net.yoshinorin.cahsper.models.request.QueryParamater
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

// testOnly net.yoshinorin.cahsper.domains.services.CommentServiceSpec
class CommentServiceSpec extends AnyWordSpec {

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val mockCommentCreator: CommentCreator = Mockito.mock(classOf[CommentCreator])
  val mockCommentFinder: CommentFinder = Mockito.mock(classOf[CommentFinder])

  when(mockCommentFinder.findById(UUID.fromString("04b89f7a-6d0c-46c5-87a2-2a35307765bf")))
    .thenReturn(Future(Some(Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "yoshinorin", "This is a test one.", 1567814290))))

  when(mockCommentFinder.getAll(QueryParamater()))
    .thenReturn(
      Future(
        Seq(
          Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "yoshinorin", "This is a test one.", 1567814290),
          Comments("cc82737d-769d-11eb-a81e-663f66aa018c", "yoshinorin", "This is a test two.", 1567814391)
        )
      )
    )

  when(mockCommentFinder.findByUserName(UserName("yoshinorin"), QueryParamater()))
    .thenReturn(
      Future(
        Seq(
          Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "yoshinorin", "This is a test one.", 1567814290),
          Comments("cc82737d-769d-11eb-a81e-663f66aa018c", "yoshinorin", "This is a test two.", 1567814391)
        )
      )
    )

  when(mockCommentFinder.findByUserName(UserName("JhonDue"), QueryParamater()))
    .thenReturn(
      Future(
        Seq(
          Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "JhonDue", "This is a test one.", 1567814290),
          Comments("cc82737d-769d-11eb-a81e-663f66aa018c", "JhonDue", "This is a test two.", 1567814391)
        )
      )
    )

  when(mockCommentCreator.create(Comments("cc827369-769d-11eb-a81e-663f66aa018c", "yoshinorin", "This is a test three.")))
    .thenReturn(Future(UUID.fromString("cc827369-769d-11eb-a81e-663f66aa018c")))

  val commentService: CommentService = new CommentService(mockCommentCreator, mockCommentFinder)

  "CommentService" should {

    "find comments instance when call findById with an argument is 1" in {
      val result = Await.result(commentService.findById(UUID.fromString("04b89f7a-6d0c-46c5-87a2-2a35307765bf")), Duration.Inf)
      assert(result.contains(Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "yoshinorin", "This is a test one.", 1567814290)))
    }

    "get all comments when call getAll" in {
      val result = Await.result(commentService.getAll(QueryParamater()), Duration.Inf)
      assert(
        result == Seq(
          Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "yoshinorin", "This is a test one.", 1567814290),
          Comments("cc82737d-769d-11eb-a81e-663f66aa018c", "yoshinorin", "This is a test two.", 1567814391)
        )
      )
    }

    "get all comments by userName when call findByUserName" in {
      val result = Await.result(commentService.findByUserName(UserName("yoshinorin"), QueryParamater()), Duration.Inf)
      assert(
        result == Seq(
          Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "yoshinorin", "This is a test one.", 1567814290),
          Comments("cc82737d-769d-11eb-a81e-663f66aa018c", "yoshinorin", "This is a test two.", 1567814391)
        )
      )
      val result2 = Await.result(commentService.findByUserName(UserName("JhonDue"), QueryParamater()), Duration.Inf)
      assert(
        result2 == Seq(
          Comments("04b89f7a-6d0c-46c5-87a2-2a35307765bf", "JhonDue", "This is a test one.", 1567814290),
          Comments("cc82737d-769d-11eb-a81e-663f66aa018c", "JhonDue", "This is a test two.", 1567814391)
        )
      )
    }

    /*
    TODO: test failure
    "create new comment" in {
      commentService.create(Users("yoshinorin"), CreateCommentRequestFormat("This is a test three.")).onComplete {
        case Success(comment: Comments) => assert(comment.id == 3)
        case Failure(_) => // Nothing to do
      }
    }
   */
  }

}
