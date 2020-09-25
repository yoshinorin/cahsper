package net.yoshinorin.cahsper.application.users

import net.yoshinorin.cahsper.domains.models.users.{UserRepository, Users}
import scala.concurrent.{ExecutionContext, Future}

class UserCreator(userRepository: UserRepository)(implicit executionContext: ExecutionContext) {

  def create(user: Users): Future[Users] = Future {
    userRepository.insert(user)
  }

}
