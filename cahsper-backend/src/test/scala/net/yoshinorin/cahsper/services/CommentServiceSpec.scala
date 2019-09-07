package net.yoshinorin.cahsper.services

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import net.yoshinorin.cahsper.models.db.{CommentRepository, Comments}
import org.mockito.Mockito._
import org.scalatest.WordSpec
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success}

// testOnly *CommentServiceSpec
class CommentServiceSpec extends WordSpec with MockitoSugar {

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

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

  "CommentService" should {

    "find comments instance when call findById with an argument is 1" in {
      val result = Await.result(commentService.findById(1), Duration.Inf)
      assert(result == Some(Comments(1, "YoshinoriN", "This is a test one.", 1567814290)))
    }

    "get all comments when call getAll" in {
      val result = Await.result(commentService.getAll, Duration.Inf)
      assert(
        result == Seq(
          Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
          Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
        )
      )
    }

    "create new comment" in {
      commentService.create("YoshinoriN", "This is a test three.").onComplete {
        case Success(comment) => assert(comment.id == 3)
        case Failure(exception) => // Nothing to do
      }
    }

  }

}
