package net.yoshinorin.cahsper.models.request

import io.circe.Decoder
import io.circe.parser.decode
import io.circe.generic.semiauto.deriveDecoder
import net.yoshinorin.cahsper.definitions.Messages
import org.slf4j.LoggerFactory

trait BaseCommentRequestFormat {}

sealed abstract class CommentRequestFormat[T](comment: String) extends BaseCommentRequestFormat {

  /**
   * Validate comment field
   *
   * @return
   */
  protected def validateComment: Either[Messages, T]

  /**
   * Validate all field
   *
   * @return
   */
  protected def validate: Either[Messages, CommentRequestFormat[T]]

}

/**
 * Case class for create comment
 *
 * @param comment user's comment
 */
case class CreateCommentFormat(
  comment: String
) extends CommentRequestFormat[String](comment) {

  /**
   * Validate comment for create new its
   *
   * @return
   */
  override def validateComment: Either[Messages, String] = {
    if (comment.trim.length < 3) {
      Left(CommentRequestFormat.requireCommentMinMessage)
    } else if (comment.trim.length > 255) {
      Left(CommentRequestFormat.requireCommentMaxMessage)
    } else {
      Right(comment)
    }
  }

  /**
   * Validate all field for create new comment
   *
   * @return
   */
  override def validate: Either[Messages, CreateCommentFormat] = {
    for {
      _ <- validateComment
    } yield {
      this.copy()
    }
  }

}

object CommentRequestFormat {

  private val logger = LoggerFactory.getLogger(this.getClass)

  val requireCommentMinMessage: Messages = Messages("Comment must be more 3 characters.")
  val requireCommentMaxMessage: Messages = Messages("Comment must be less than 256 characters.")

  implicit val decodeCreateCommentFormat: Decoder[CreateCommentFormat] = deriveDecoder[CreateCommentFormat]

  /**
   * Convert string JSON to CommentRequestFormat case class
   *
   * @param string
   * @param decoder
   * @tparam T
   * @return
   */
  def convertFromJsonString[T <: BaseCommentRequestFormat](string: String)(implicit decoder: Decoder[T]): Either[Messages, T] = {
    decode[T](string) match {
      case Right(commentRequestFormat) => Right(commentRequestFormat)
      case Left(error) => {
        logger.error(error.getMessage)
        Left(Messages(error.getMessage))
      }
    }
  }

}
