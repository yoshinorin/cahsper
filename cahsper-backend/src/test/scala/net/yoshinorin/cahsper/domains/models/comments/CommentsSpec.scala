package net.yoshinorin.cahsper.domains.models.comments

import java.time.{Instant, ZoneOffset, ZonedDateTime}
import java.util.UUID

import org.scalatest.wordspec.AnyWordSpec

// testOnly net.yoshinorin.cahsper.domains.models.comments.CommentsSpec
class CommentsSpec extends AnyWordSpec {

  "Comments" should {

    "default instance" in {
      val currentUTCDateTime = ZonedDateTime.now(ZoneOffset.UTC)
      val comment = Comments(userName = "yoshinorin", comment = "This is a test")
      val instanceUTCDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(comment.createdAt), ZoneOffset.UTC)

      assert(UUID.fromString(comment.id).isInstanceOf[UUID])
      assert(comment.userName == "yoshinorin")
      assert(comment.comment == "This is a test")
      assert(instanceUTCDateTime.getYear == currentUTCDateTime.getYear)
      assert(instanceUTCDateTime.getMonth == currentUTCDateTime.getMonth)
      assert(instanceUTCDateTime.getDayOfMonth == currentUTCDateTime.getDayOfMonth)
      assert(instanceUTCDateTime.getHour == currentUTCDateTime.getHour)
    }

    "specific id and createdAt" in {
      val comment = Comments("cc827369-769d-11eb-a81e-663f66aa018c", "yoshinorin", "This is a test", 1567814286)
      assert(comment.id == "cc827369-769d-11eb-a81e-663f66aa018c")
      assert(comment.userName == "yoshinorin")
      assert(comment.comment == "This is a test")
      assert(comment.createdAt == 1567814286)
    }
  }

}
