package net.yoshinorin.cahsper.services

import akka.actor.ActorSystem
import net.yoshinorin.cahsper.domains.models.users.{UserName, Users}
import net.yoshinorin.cahsper.domains.services.CommentService
import net.yoshinorin.cahsper.models.db.{CommentRepository, Comments}
import net.yoshinorin.cahsper.models.request.{CreateCommentRequestFormat, QueryParamater}
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success}

// testOnly net.yoshinorin.cahsper.services.CommentServiceSpec
class CommentServiceSpec extends AnyWordSpec {

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val mockCommentRepository: CommentRepository = Mockito.mock(classOf[CommentRepository])

  when(mockCommentRepository.findById(1))
    .thenReturn(Some(Comments(1, "YoshinoriN", "This is a test one.", 1567814290)))

  when(mockCommentRepository.getAll(QueryParamater()))
    .thenReturn(
      Seq(
        Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
        Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
      )
    )

  when(mockCommentRepository.findByUserName(UserName("YoshinoriN"), QueryParamater()))
    .thenReturn(
      Seq(
        Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
        Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
      )
    )

  when(mockCommentRepository.findByUserName(UserName("JhonDue"), QueryParamater()))
    .thenReturn(
      Seq(
        Comments(1, "JhonDue", "This is a test one.", 1567814290),
        Comments(2, "JhonDue", "This is a test two.", 1567814391)
      )
    )

  when(mockCommentRepository.insert(Comments(3, "YoshinoriN", "This is a test three.")))
    .thenReturn(3)

  val commentService: CommentService = new CommentService(mockCommentRepository)

  "CommentService" should {

    "find comments instance when call findById with an argument is 1" in {
      val result = Await.result(commentService.findById(1), Duration.Inf)
      assert(result.contains(Comments(1, "YoshinoriN", "This is a test one.", 1567814290)))
    }

    "get all comments when call getAll" in {
      val result = Await.result(commentService.getAll(QueryParamater()), Duration.Inf)
      assert(
        result == Seq(
          Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
          Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
        )
      )
    }

    "get all comments by userName when call findByUserName" in {
      val result = Await.result(commentService.findByUserName(UserName("YoshinoriN"), QueryParamater()), Duration.Inf)
      assert(
        result == Seq(
          Comments(1, "YoshinoriN", "This is a test one.", 1567814290),
          Comments(2, "YoshinoriN", "This is a test two.", 1567814391)
        )
      )
      val result2 = Await.result(commentService.findByUserName(UserName("JhonDue"), QueryParamater()), Duration.Inf)
      assert(
        result2 == Seq(
          Comments(1, "JhonDue", "This is a test one.", 1567814290),
          Comments(2, "JhonDue", "This is a test two.", 1567814391)
        )
      )
    }

    "create new comment" in {
      commentService.create(Users("YoshinoriN"), CreateCommentRequestFormat("This is a test three.")).onComplete {
        case Success(comment) => assert(comment.id == 3)
        case Failure(exception) => // Nothing to do
      }
    }

  }

}
