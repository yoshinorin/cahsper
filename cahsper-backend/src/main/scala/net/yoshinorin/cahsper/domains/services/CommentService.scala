package net.yoshinorin.cahsper.domains.services

import net.yoshinorin.cahsper.domains.models.comments.{CommentRepository, Comments, CreateCommentRequestFormat}
import net.yoshinorin.cahsper.domains.models.users.{UserName, Users}
import net.yoshinorin.cahsper.models.request.QueryParamater

import scala.concurrent.{ExecutionContext, Future}

class CommentService(commentRepository: CommentRepository)(implicit executeContext: ExecutionContext) {

  private[this] def create(comment: Comments): Future[Int] = Future {
    commentRepository.insert(comment)
  }

  /**
   * Create comment
   *
   * @param user User
   * @param createCommentFormat
   * @return
   */
  def create(user: Users, createCommentFormat: CreateCommentRequestFormat): Future[Comments] = {

    val comment = Comments(userName = user.name, comment = createCommentFormat.comment)
    for {
      mayBeCommentId <- this.create(comment)
      mayBeComment <- this.findById(mayBeCommentId)
    } yield mayBeComment.head

  }

  /**
   * Find comment by id
   *
   * @param id
   * @return
   */
  def findById(id: Int): Future[Option[Comments]] = Future {
    commentRepository.findById(id)
  }

  /**
   * Find comment by userName
   *
   * @param userName
   * @param queryParamater
   * @return
   */
  def findByUserName(userName: UserName, queryParamater: QueryParamater): Future[Seq[Comments]] = Future {
    commentRepository.findByUserName(userName, queryParamater)
  }

  /**
   * Get all comments
   *
   * @param queryParamater
   * @return
   */
  def getAll(queryParamater: QueryParamater): Future[Seq[Comments]] = Future {
    commentRepository.getAll(queryParamater)
  }

}
