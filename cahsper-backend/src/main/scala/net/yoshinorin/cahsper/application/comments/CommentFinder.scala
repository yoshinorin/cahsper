package net.yoshinorin.cahsper.application.comments

import net.yoshinorin.cahsper.domains.models.comments.{CommentRepository, Comments}
import net.yoshinorin.cahsper.domains.models.users.UserName
import net.yoshinorin.cahsper.models.request.QueryParamater

import scala.concurrent.{ExecutionContext, Future}

class CommentFinder(commentRepository: CommentRepository)(implicit executionContext: ExecutionContext) {

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
