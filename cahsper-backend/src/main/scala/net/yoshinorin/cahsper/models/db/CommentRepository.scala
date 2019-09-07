package net.yoshinorin.cahsper.models.db

import net.yoshinorin.cahsper.services.QuillService

class CommentRepository extends QuillService {

  import ctx._

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
   * Get all records in the comments table
   *
   * TODO: implement like SQL limit and should change function name
   * @return all comments
   */
  def getAll: Seq[Comments] = {
    run(query[Comments])
  }

}
