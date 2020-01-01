package net.yoshinorin.cahsper.models.db

import net.yoshinorin.cahsper.infrastructure.DataBaseContext
import net.yoshinorin.cahsper.models.User
import net.yoshinorin.cahsper.models.request.QueryParamater

class CommentRepository extends DataBaseContext[Comments] {

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
   * Find comment by userName
   *
   * @param user
   * @param queryParamater
   * @return
   */
  def findByUserName(user: User, queryParamater: QueryParamater): Seq[Comments] = {
    run(
      sortByCreatedAt(
        filterWithQueryParam(queryParamater),
        queryParamater.order
      ).filter(comment => comment.userName == lift(user.name))
    )
  }

  /**
   * Get all records in the comments table
   *
   * @param queryParamater
   * @return all comments
   */
  def getAll(queryParamater: QueryParamater): Seq[Comments] = {
    run(sortByCreatedAt(filterWithQueryParam(queryParamater), queryParamater.order))
  }

}
