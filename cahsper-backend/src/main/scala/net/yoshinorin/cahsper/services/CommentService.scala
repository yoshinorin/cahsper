package net.yoshinorin.cahsper.services

import scala.concurrent.{ExecutionContext, Future}
import net.yoshinorin.cahsper.models.db.{CommentRepository, Comments}

class CommentService(commentRepository: CommentRepository)(implicit executeContext: ExecutionContext) {

  private[this] def create(comment: Comments): Future[Int] = Future {
    commentRepository.insert(comment)
  }

  /**
   * Create comment
   *
   * @param user user name
   * @param userComment user's comment
   * @return
   */
  def create(user: String, userComment: String): Future[Comments] = {

    val comment = Comments(user = user, comment = userComment)
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
   * Get all comments
   *
   * @return
   */
  def getAll: Future[Seq[Comments]] = Future {
    commentRepository.getAll
  }

}
