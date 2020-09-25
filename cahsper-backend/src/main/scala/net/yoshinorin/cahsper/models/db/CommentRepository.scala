package net.yoshinorin.cahsper.models.db

import net.yoshinorin.cahsper.domains.users.{UserName, Users}
import net.yoshinorin.cahsper.infrastructure.quill.OrderType.OrderConverter
import net.yoshinorin.cahsper.infrastructure.quill.DataBaseContext
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
   * @param userName
   * @param queryParamater
   * @return
   */
  def findByUserName(userName: UserName, queryParamater: QueryParamater): Seq[Comments] = {
    run(
      sortByCreatedAt(
        filterWithQueryParam(queryParamater),
        queryParamater.order.toOrder
      ).filter(comment => comment.userName == lift(userName.value))
    )
  }

  /**
   * Get all records in the comments table
   *
   * @param queryParamater
   * @return all comments
   */
  def getAll(queryParamater: QueryParamater): Seq[Comments] = {
    run(sortByCreatedAt(filterWithQueryParam(queryParamater), queryParamater.order.toOrder))
  }

}
