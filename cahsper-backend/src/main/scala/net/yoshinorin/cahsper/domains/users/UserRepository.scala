package net.yoshinorin.cahsper.domains.users

import net.yoshinorin.cahsper.infrastructure.DataBaseContext

class UserRepository extends DataBaseContext[Users] {

  import ctx._

  /**
   * Create user
   *
   * @param data Users Instance
   * @return created user name
   */
  def insert(data: Users): Users = {
    //run(query[Users].insert(lift(data)).returning(_.name))
    run(query[Users].insert(lift(data))) //TODO: Mysql hasn't returning clause
    data
  }

  /**
   * Find user by Name
   *
   * @param user
   * @return Users
   */
  def findByName(user: User): Option[Users] = {
    run(query[Users].filter(u => u.name == lift(user.name))).headOption
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
