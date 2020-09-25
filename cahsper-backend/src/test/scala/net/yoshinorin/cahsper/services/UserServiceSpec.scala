package net.yoshinorin.cahsper.services

import akka.actor.ActorSystem
import net.yoshinorin.cahsper.application.users.{UserCreator, UserFinder}
import net.yoshinorin.cahsper.domains.models.users.{UserName, UserRepository, Users}
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

// testOnly net.yoshinorin.cahsper.services.UserServiceSpec
class UserServiceSpec extends AnyWordSpec {

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val mockUserCreator: UserCreator = Mockito.mock(classOf[UserCreator])
  val mockUserFinder: UserFinder = Mockito.mock(classOf[UserFinder])
  val mockUserRepository: UserRepository = Mockito.mock(classOf[UserRepository])

  when(mockUserFinder.findByName(UserName("YoshinoriN")))
    .thenReturn(Future(Option(Users("YoshinoriN", 1567814290))))

  when(mockUserCreator.create(Users("YoshinoriN")))
    .thenReturn(Future(Users("YoshinoriN")))

  when(mockUserFinder.getAll)
    .thenReturn(
      Future(
        Seq(
          Users("YoshinoriN", 1567814290),
          Users("NoshinoriN", 1567814391)
        )
      )
    )

  val userService: UserService = new UserService(mockUserCreator, mockUserFinder)

  "UserService" should {

    "find users instance when call findById with an argument is 1" in {
      val result = Await.result(userService.findByName(UserName("YoshinoriN")), Duration.Inf)
      assert(result.contains(Users("YoshinoriN", 1567814290)))
    }

    "create new user" in {
      userService.create(UserName("YoshinoriN")).onComplete {
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
