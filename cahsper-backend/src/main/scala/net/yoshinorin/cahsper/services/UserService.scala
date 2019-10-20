package net.yoshinorin.cahsper.services

import net.yoshinorin.cahsper.models.db.{UserRepository, Users}
import scala.concurrent.{ExecutionContext, Future}

class UserService(userRepository: UserRepository)(implicit executionContext: ExecutionContext) {

  private[this] def create(user: Users): Future[String] = Future {
    userRepository.insert(user)
  }

  /**
   * Create User
   *
   * @param userName user name
   * @return
   */
  def create(userName: String): Future[Users] = {
    // TODO: Check user already exists or not

    val user = Users(userName)
    for {
      maybeUserName <- this.create(user)
      maybeUser <- this.findByName(maybeUserName)
    } yield maybeUser.head

  }

  /**
   * Find user by name
   *
   * @param name user name
   * @return
   */
  def findByName(name: String): Future[Option[Users]] = Future {
    userRepository.findByName(name)
  }

  /**
   * Get all users
   *
   * @return
   */
  def getAll: Future[Seq[Users]] = Future {
    userRepository.getAll
  }

}
