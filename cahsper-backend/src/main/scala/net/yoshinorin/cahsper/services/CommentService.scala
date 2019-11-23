package net.yoshinorin.cahsper.services

import net.yoshinorin.cahsper.models.User

import scala.concurrent.{ExecutionContext, Future}
import net.yoshinorin.cahsper.models.db.{CommentRepository, Comments}
import net.yoshinorin.cahsper.models.request.CreateCommentRequestFormat

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
  def create(user: User, createCommentFormat: CreateCommentRequestFormat): Future[Comments] = {

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
   * @param user
   * @return
   */
  def findByUserName(user: User): Future[Seq[Comments]] = Future {
    commentRepository.findByUserName(user)
  }

  /**
   * Get all comments
   *
   * @return
   */
  def getAll: Future[Seq[Comments]] = Future {
    commentRepository.getAll
  }

}
