package net.yoshinorin.cahsper.application.users

import net.yoshinorin.cahsper.domains.users.{UserName, UserRepository, Users}

import scala.concurrent.{ExecutionContext, Future}

class UserFinder(userRepository: UserRepository)(implicit executionContext: ExecutionContext) {

  /**
   * Find User by userName
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
