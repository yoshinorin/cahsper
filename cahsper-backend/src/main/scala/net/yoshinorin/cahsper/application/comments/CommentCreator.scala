package net.yoshinorin.cahsper.application.comments

import java.util.UUID

import net.yoshinorin.cahsper.domains.models.comments.{CommentRepository, Comments}

import scala.concurrent.{ExecutionContext, Future}

class CommentCreator(commentRepository: CommentRepository)(implicit executionContext: ExecutionContext) {

  def create(comment: Comments): Future[UUID] = Future {
    commentRepository.insert(comment)
  }

}
