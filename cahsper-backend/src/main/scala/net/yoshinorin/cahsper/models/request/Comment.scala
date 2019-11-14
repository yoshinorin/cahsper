package net.yoshinorin.cahsper.models.request

import io.circe.Decoder
import io.circe.parser.decode
import io.circe.generic.semiauto.deriveDecoder
import net.yoshinorin.cahsper.models.Message
import org.slf4j.LoggerFactory

trait BaseCommentRequestFormat {}

sealed abstract class CommentRequestFormat[T](comment: String) extends BaseCommentRequestFormat {

  /**
   * Validate comment field
   *
   * @return
   */
  protected def validateComment: Either[Message, T]

  /**
   * Validate all field
   *
   * @return
   */
  protected def validate: Either[Message, CommentRequestFormat[T]]

}

/**
 * Case class for create comment
 *
 * @param comment user's comment
 */
case class CreateCommentRequestFormat(
  comment: String
) extends CommentRequestFormat[String](comment) {

  /**
   * Validate comment for create new its
   *
   * @return
   */
  override def validateComment: Either[Message, String] = {
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
  override def validate: Either[Message, CreateCommentRequestFormat] = {
    for {
      _ <- validateComment
    } yield {
      this.copy()
    }
  }

}

object CommentRequestFormat {

  private val logger = LoggerFactory.getLogger(this.getClass)

  val requireCommentMinMessage: Message = Message("Comment must be more 3 characters.")
  val requireCommentMaxMessage: Message = Message("Comment must be less than 256 characters.")

  implicit val decodeCreateCommentRequestFormat: Decoder[CreateCommentRequestFormat] = deriveDecoder[CreateCommentRequestFormat]

  /**
   * Convert string JSON to CommentRequestFormat case class
   *
   * @param string
   * @param decoder
   * @tparam T
   * @return
   */
  def convertFromJsonString[T <: BaseCommentRequestFormat](string: String)(implicit decoder: Decoder[T]): Either[Message, T] = {
    decode[T](string) match {
      case Right(commentRequestFormat) => Right(commentRequestFormat)
      case Left(error) => {
        logger.error(error.getMessage)
        Left(Message(error.getMessage))
      }
    }
  }

}
