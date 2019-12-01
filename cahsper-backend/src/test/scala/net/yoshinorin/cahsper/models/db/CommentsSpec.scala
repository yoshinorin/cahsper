package net.yoshinorin.cahsper.models.db

import java.time.{Instant, ZoneOffset, ZonedDateTime}

import org.scalatest.wordspec.AnyWordSpec

// testOnly *CommentsSpec
class CommentsSpec extends AnyWordSpec {

  "Comments" should {

    "default instance" in {
      val currentUTCDateTime = ZonedDateTime.now(ZoneOffset.UTC)
      val comment = Comments(userName = "YoshinoriN", comment = "This is a test")
      val instanceUTCDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(comment.createdAt), ZoneOffset.UTC)

      assert(comment.id == 0)
      assert(comment.userName == "YoshinoriN")
      assert(comment.comment == "This is a test")
      assert(instanceUTCDateTime.getYear == currentUTCDateTime.getYear)
      assert(instanceUTCDateTime.getMonth == currentUTCDateTime.getMonth)
      assert(instanceUTCDateTime.getDayOfMonth == currentUTCDateTime.getDayOfMonth)
      assert(instanceUTCDateTime.getHour == currentUTCDateTime.getHour)
    }

    "specific id and createdAt" in {
      val comment = Comments(9, "YoshinoriN", "This is a test", 1567814286)
      assert(comment.id == 9)
      assert(comment.userName == "YoshinoriN")
      assert(comment.comment == "This is a test")
      assert(comment.createdAt == 1567814286)
    }
  }

}
