package net.yoshinorin.cahsper.services

import net.yoshinorin.cahsper.application.users.{UserCreator, UserFinder}
import net.yoshinorin.cahsper.domains.users.{UserName, Users}

import scala.concurrent.{ExecutionContext, Future}

class UserService(userCreator: UserCreator, userFinder: UserFinder)(implicit executionContext: ExecutionContext) {

  /**
   * Create User from UserName
   *
   * @param userName
   * @return
   */
  def create(userName: UserName): Future[Users] = {
    // TODO: Check userName already exists or not

    for {
      maybeUser <- userCreator.create(Users(userName.value))
      maybeUser <- userFinder.findByName(UserName(maybeUser.name))
    } yield maybeUser.head

  }

  /**
   * Find User by value
   *
   * @param userName
   * @return
   */
  def findByName(userName: UserName): Future[Option[Users]] = userFinder.findByName(userName)

  /**
   * Get all users
   *
   * @return
   */
  def getAll: Future[Seq[Users]] = userFinder.getAll

}
