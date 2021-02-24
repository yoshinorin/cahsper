package net.yoshinorin.cahsper.domains.services

import java.util.UUID

import net.yoshinorin.cahsper.application.comments.{CommentCreator, CommentFinder}
import net.yoshinorin.cahsper.domains.models.comments.{Comments, CreateCommentRequestFormat}
import net.yoshinorin.cahsper.domains.models.users.{UserName, Users}
import net.yoshinorin.cahsper.models.request.QueryParamater

import scala.concurrent.{ExecutionContext, Future}

class CommentService(commentCreator: CommentCreator, commentFinder: CommentFinder)(implicit executeContext: ExecutionContext) {

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
      mayBeCommentId <- commentCreator.create(comment)
      mayBeComment <- commentFinder.findById(mayBeCommentId)
    } yield mayBeComment.head

  }

  /**
   * Find comment by uuid
   *
   * @param uuid
   * @return
   */
  def findById(uuid: UUID): Future[Option[Comments]] = {
    commentFinder.findById(uuid)
  }

  /**
   * Find comment by userName
   *
   * @param userName
   * @param queryParamater
   * @return
   */
  def findByUserName(userName: UserName, queryParamater: QueryParamater): Future[Seq[Comments]] = {
    commentFinder.findByUserName(userName, queryParamater)
  }

  /**
   * Get all comments
   *
   * @param queryParamater
   * @return
   */
  def getAll(queryParamater: QueryParamater): Future[Seq[Comments]] = {
    commentFinder.getAll(queryParamater)
  }

}
