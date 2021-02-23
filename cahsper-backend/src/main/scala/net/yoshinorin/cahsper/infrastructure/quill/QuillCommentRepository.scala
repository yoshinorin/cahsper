package net.yoshinorin.cahsper.infrastructure.quill

import net.yoshinorin.cahsper.domains.models.comments.{CommentRepository, Comments}
import net.yoshinorin.cahsper.domains.models.users.UserName
import net.yoshinorin.cahsper.infrastructure.quill.OrderType.OrderConverter
import net.yoshinorin.cahsper.models.request.QueryParamater

class QuillCommentRepository extends QuillDataBaseContext[Comments] with CommentRepository {

  import ctx._

  def insert(data: Comments): Int = {
    run(query[Comments].insert(lift(data)).returningGenerated(_.id))
  }

  def findById(id: Int): Option[Comments] = {
    run(query[Comments].filter(comment => comment.id == lift(id))).headOption
  }

  def findByUserName(userName: UserName, queryParamater: QueryParamater): Seq[Comments] = {
    run(
      sortByCreatedAt(
        filterWithQueryParam(queryParamater),
        queryParamater.order.toOrder
      ).filter(comment => comment.userName.toLowerCase == lift(userName.value.toLowerCase))
    )
  }

  def getAll(queryParamater: QueryParamater): Seq[Comments] = {
    run(sortByCreatedAt(filterWithQueryParam(queryParamater), queryParamater.order.toOrder))
  }

}
