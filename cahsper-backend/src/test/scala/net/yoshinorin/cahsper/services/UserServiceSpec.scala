package net.yoshinorin.cahsper.services

import akka.actor.ActorSystem
import net.yoshinorin.cahsper.models.User
import net.yoshinorin.cahsper.models.db.{UserRepository, Users}
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success}

// testOnly net.yoshinorin.cahsper.services.UserServiceSpec
class UserServiceSpec extends AnyWordSpec {

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val mockUserRepository: UserRepository = Mockito.mock(classOf[UserRepository])

  when(mockUserRepository.findByName("YoshinoriN"))
    .thenReturn(Some(Users("YoshinoriN", 1567814290)))

  when(mockUserRepository.insert(Users("YoshinoriN")))
    .thenReturn("YoshinoriN")

  when(mockUserRepository.getAll)
    .thenReturn(
      Seq(
        Users("YoshinoriN", 1567814290),
        Users("NoshinoriN", 1567814391)
      )
    )

  val userService: UserService = new UserService(mockUserRepository)

  "UserService" should {

    "find users instance when call findById with an argument is 1" in {
      val result = Await.result(userService.findByName("YoshinoriN"), Duration.Inf)
      assert(result == Some(Users("YoshinoriN", 1567814290)))
    }

    "create new user" in {
      userService.create(User("YoshinoriN")).onComplete {
        case Success(user) => assert(user.name == "YoshinoriN")
        case Failure(exception) => // Nothing to do
      }
    }

    "get all users when call getAll" in {
      val result = Await.result(userService.getAll, Duration.Inf)
      assert(
        result == Seq(
          Users("YoshinoriN", 1567814290),
          Users("NoshinoriN", 1567814391)
        )
      )
    }

  }

}
