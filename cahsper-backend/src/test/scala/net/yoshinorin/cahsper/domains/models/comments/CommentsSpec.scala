package net.yoshinorin.cahsper.domains.models.comments

import java.time.{Instant, ZoneOffset, ZonedDateTime}

import org.scalatest.wordspec.AnyWordSpec

// testOnly net.yoshinorin.cahsper.domains.models.comments.CommentsSpec
class CommentsSpec extends AnyWordSpec {

  "Comments" should {

    "default instance" in {
      val currentUTCDateTime = ZonedDateTime.now(ZoneOffset.UTC)
      val comment = Comments(userName = "yoshinorin", comment = "This is a test")
      val instanceUTCDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(comment.createdAt), ZoneOffset.UTC)

      assert(comment.id == 0)
      assert(comment.userName == "yoshinorin")
      assert(comment.comment == "This is a test")
      assert(instanceUTCDateTime.getYear == currentUTCDateTime.getYear)
      assert(instanceUTCDateTime.getMonth == currentUTCDateTime.getMonth)
      assert(instanceUTCDateTime.getDayOfMonth == currentUTCDateTime.getDayOfMonth)
      assert(instanceUTCDateTime.getHour == currentUTCDateTime.getHour)
    }

    "specific id and createdAt" in {
      val comment = Comments(9, "yoshinorin", "This is a test", 1567814286)
      assert(comment.id == 9)
      assert(comment.userName == "yoshinorin")
      assert(comment.comment == "This is a test")
      assert(comment.createdAt == 1567814286)
    }
  }

}
