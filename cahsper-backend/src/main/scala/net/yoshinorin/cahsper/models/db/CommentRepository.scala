package net.yoshinorin.cahsper.models.db

import net.yoshinorin.cahsper.infrastructure.DataBaseContext
import net.yoshinorin.cahsper.models.User

class CommentRepository(dbCtx: DataBaseContext) {

  import dbCtx.ctx._

  /**
   * Create comment
   *
   * @param data Comments Instance
   * @return created comment id
   */
  def insert(data: Comments): Int = {
    run(query[Comments].insert(lift(data)).returningGenerated(_.id))
  }

  /**
   * Find comment by id
   *
   * @param id
   * @return Comment
   */
  def findById(id: Int): Option[Comments] = {
    run(query[Comments].filter(comment => comment.id == lift(id))).headOption
  }

  /**
   * Find comment by userName
   *
   * @param user
   * @return
   */
  def findByUserName(user: User): Seq[Comments] = {
    run(query[Comments].filter(comment => comment.userName == lift(user.name)))
  }

  /**
   * Get all records in the comments table
   *
   * TODO: implement like SQL limit and should change function name
   * @return all comments
   */
  def getAll: Seq[Comments] = {
    run(query[Comments])
  }

}
