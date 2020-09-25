package net.yoshinorin.cahsper.services

import net.yoshinorin.cahsper.domains.users.{User, Users, UserRepository}

import scala.concurrent.{ExecutionContext, Future}

class UserService(userRepository: UserRepository)(implicit executionContext: ExecutionContext) {

  private[this] def create(user: Users): Future[Users] = Future {
    userRepository.insert(user)
  }

  /**
   * Create User
   *
   * @param user
   * @return
   */
  def create(user: User): Future[Users] = {
    // TODO: Check user already exists or not

    for {
      maybeUser <- this.create(Users(user.name))
      maybeUser <- this.findByName(User(maybeUser.name))
    } yield maybeUser.head

  }

  /**
   * Find user by name
   *
   * @param user user name
   * @return
   */
  def findByName(user: User): Future[Option[Users]] = Future {
    userRepository.findByName(user)
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
