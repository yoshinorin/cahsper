package net.yoshinorin.cahsper.models.db

import org.scalatest.WordSpec
import net.yoshinorin.cahsper.models.request.{CommentRequestFormat, CreateCommentFormat}

// testOnly *CommentRequestFormatSpec
class CommentRequestFormatSpec extends WordSpec {

  val commentMaxString = new StringBuilder
  for (i <- 0 until 255) {
    commentMaxString.append("a")
  }

  val commentExceedMaxString = new StringBuilder
  for (i <- 0 until 256) {
    commentExceedMaxString.append("a")
  }

  "CommentRequestFormat" should {

    "convert success when comment is max length" in {
      val payloadJson: String = s"""{"comment" : "${commentMaxString}"}"""
      val createCommentRequestFormat = CommentRequestFormat.convertFromJsonString[CreateCommentFormat](payloadJson)
      val result = createCommentRequestFormat.right.get.validate

      assert(result.isRight)
    }

    "convert success when comment length is three" in {
      val payloadJson: String = s"""{"comment" : "123"}"""
      val createCommentRequestFormat = CommentRequestFormat.convertFromJsonString[CreateCommentFormat](payloadJson)
      val result = createCommentRequestFormat.right.get.validate

      assert(result.isRight)
    }

    "convert failed when comment is exceed max length" in {
      val payloadJson: String = s"""{"comment" : "${commentExceedMaxString}"}"""
      val createCommentRequestFormat = CommentRequestFormat.convertFromJsonString[CreateCommentFormat](payloadJson)
      val result = createCommentRequestFormat.right.get.validate

      assert(result.left.get.messages == CommentRequestFormat.requireCommentMaxMessage.messages)
    }

    "convert failed when comment is too short" in {
      val payloadJson: String = """{"comment" : "aa"}"""
      val createCommentRequestFormat = CommentRequestFormat.convertFromJsonString[CreateCommentFormat](payloadJson)
      val result = createCommentRequestFormat.right.get.validate

      assert(result.left.get.messages == CommentRequestFormat.requireCommentMinMessage.messages)
    }

    "convert failed when comment key does not extist in JSON" in {
      val payloadJson: String = """{"dummy" : "not exitst needed key"}"""
      val createCommentRequestFormat = CommentRequestFormat.convertFromJsonString[CreateCommentFormat](payloadJson)

      assert(createCommentRequestFormat.isLeft)
    }

    "convert failed when string in not a JSON" in {
      val payloadJson: String = """{NOT a JSON}"""
      val createCommentRequestFormat = CommentRequestFormat.convertFromJsonString[CreateCommentFormat](payloadJson)

      assert(createCommentRequestFormat.isLeft)
    }

  }

}
