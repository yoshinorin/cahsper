package net.yoshinorin.cahsper.services

import net.yoshinorin.cahsper.domains.users.{UserName, Users, UserRepository}

import scala.concurrent.{ExecutionContext, Future}

class UserService(userRepository: UserRepository)(implicit executionContext: ExecutionContext) {

  private[this] def create(user: Users): Future[Users] = Future {
    userRepository.insert(user)
  }

  /**
   * Create User from UserName
   *
   * @param userName
   * @return
   */
  def create(userName: UserName): Future[Users] = {
    // TODO: Check userName already exists or not

    for {
      maybeUser <- this.create(Users(userName.value))
      maybeUser <- this.findByName(UserName(maybeUser.name))
    } yield maybeUser.head

  }

  /**
   * Find User by value
   *
   * @param userName
   * @return
   */
  def findByName(userName: UserName): Future[Option[Users]] = Future {
    userRepository.findByName(userName)
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
