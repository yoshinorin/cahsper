package net.yoshinorin.cahsper.application.comments

import net.yoshinorin.cahsper.domains.models.comments.{CommentRepository, Comments}

import scala.concurrent.{ExecutionContext, Future}

class CommentCreator(commentRepository: CommentRepository)(implicit executionContext: ExecutionContext) {

  def create(comment: Comments): Future[Int] = Future {
    commentRepository.insert(comment)
  }

}
