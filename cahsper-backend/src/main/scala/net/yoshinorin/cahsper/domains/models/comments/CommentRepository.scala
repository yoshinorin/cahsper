package net.yoshinorin.cahsper.domains.models.comments

import net.yoshinorin.cahsper.domains.models.users.UserName
import net.yoshinorin.cahsper.infrastructure.quill.OrderType.OrderConverter
import net.yoshinorin.cahsper.models.request.QueryParamater

trait CommentRepository {

  /**
   * Create comment
   *
   * @param data Comments Instance
   * @return created comment id
   */
  def insert(data: Comments): Int

  /**
   * Find comment by id
   *
   * @param id
   * @return Comment
   */
  def findById(id: Int): Option[Comments]

  /**
   * Find comment by userName
   *
   * @param userName
   * @param queryParamater
   * @return
   */
  def findByUserName(userName: UserName, queryParamater: QueryParamater): Seq[Comments]

  /**
   * Get all records in the comments table
   *
   * @param queryParamater
   * @return all comments
   */
  def getAll(queryParamater: QueryParamater): Seq[Comments]

}
