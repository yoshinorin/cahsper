package net.yoshinorin.cahsper.models.db

import net.yoshinorin.cahsper.services.QuillService

class UserRepository extends QuillService {

  import ctx._

  /**
   * Create user
   *
   * @param data Users Instance
   * @return created user name
   */
  def insert(data: Users): String = {
    //run(query[Users].insert(lift(data)).returning(_.name))
    run(query[Users].insert(lift(data))) //TODO: Mysql hasn't returning clause
    data.name
  }

  /**
   * Find user by Name
   *
   * @param name
   * @return Users
   */
  def findByName(name: String): Option[Users] = {
    run(query[Users].filter(user => user.name == lift(name))).headOption
  }

}
