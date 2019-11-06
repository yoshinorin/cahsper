package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.WordSpec

// testOnly *TopRouteSpec
class TopRouteSpec extends WordSpec with ScalatestRouteTest {

  val topRoute = new TopRoute()

  "TopRoute" should {

    "return robots.txt" in {
      Get("/robots.txt") ~> topRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`text/plain(UTF-8)`)
        assert(responseAs[String].startsWith("User-agent"))
      }

    }
  }

}
