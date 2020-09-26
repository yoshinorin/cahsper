package net.yoshinorin.cahsper.infrastructure.quill

import net.yoshinorin.cahsper.domains.models.users.{UserName, UserRepository, Users}

class QuillUserRepository extends QuillDataBaseContext[Users] with UserRepository {

  import ctx._

  /**
   * Create user
   *
   * @param data Users Instance
   * @return created User
   */
  def insert(data: Users): Users = {
    //run(query[Users].insert(lift(data)).returning(_.value))
    run(query[Users].insert(lift(data))) //TODO: Mysql hasn't returning clause
    data
  }

  /**
   * Find userName by Name
   *
   * @param userName
   * @return Users
   */
  def findByName(userName: UserName): Option[Users] = {
    run(query[Users].filter(u => u.name == lift(userName.value))).headOption
  }

  /**
   * Get all users
   *
   * TODO: implement like SQL limit and should change function name
   * @return all users
   */
  def getAll: Seq[Users] = {
    run(query[Users])
  }
}
