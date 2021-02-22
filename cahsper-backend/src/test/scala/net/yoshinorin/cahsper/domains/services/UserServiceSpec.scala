package net.yoshinorin.cahsper.domains.services

import akka.actor.ActorSystem
import net.yoshinorin.cahsper.application.users.{UserCreator, UserFinder}
import net.yoshinorin.cahsper.domains.models.users.{UserName, UserRepository, Users}
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

// testOnly net.yoshinorin.cahsper.domains.services.UserServiceSpec
class UserServiceSpec extends AnyWordSpec {

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val mockUserCreator: UserCreator = Mockito.mock(classOf[UserCreator])
  val mockUserFinder: UserFinder = Mockito.mock(classOf[UserFinder])
  val mockUserRepository: UserRepository = Mockito.mock(classOf[UserRepository])

  when(mockUserFinder.findByName(UserName("yoshinorin")))
    .thenReturn(Future(Option(Users("yoshinorin", "yoshinorin-dispname", 1567814290))))

  when(mockUserCreator.create(Users("yoshinorin", "yoshinorin")))
    .thenReturn(Future(Users("yoshinorin", "yoshinorin")))

  when(mockUserFinder.getAll)
    .thenReturn(
      Future(
        Seq(
          Users("yoshinorin", "yoshinorin-dispname", 1567814290),
          Users("NoshinoriN", "yoshinorin-dispname", 1567814391)
        )
      )
    )

  val userService: UserService = new UserService(mockUserCreator, mockUserFinder)

  "UserService" should {

    "find users instance when call findById with an argument is 1" in {
      val result = Await.result(userService.findByName(UserName("yoshinorin")), Duration.Inf)
      assert(result.contains(Users("yoshinorin", "yoshinorin-dispname", 1567814290)))
    }

    "create new user" in {
      userService.create(UserName("yoshinorin")).onComplete {
        case Success(user) => assert(user.name == "yoshinorin")
        case Failure(exception) => // Nothing to do
      }
    }

    "get all users when call getAll" in {
      val result = Await.result(userService.getAll, Duration.Inf)
      assert(
        result == Seq(
          Users("yoshinorin", "yoshinorin-dispname", 1567814290),
          Users("NoshinoriN", "yoshinorin-dispname", 1567814391)
        )
      )
    }

  }

}
