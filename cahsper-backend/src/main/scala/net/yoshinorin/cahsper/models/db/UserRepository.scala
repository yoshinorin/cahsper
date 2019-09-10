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
    run(query[Users].insert(lift(data)).returningGenerated(_.name))
  }

  /**
   * Find comment by user
   *
   * @param name
   * @return Comment
   */
  def findById(name: String): Option[Users] = {
    run(query[Users].filter(comment => comment.name == lift(name))).headOption
  }

}
