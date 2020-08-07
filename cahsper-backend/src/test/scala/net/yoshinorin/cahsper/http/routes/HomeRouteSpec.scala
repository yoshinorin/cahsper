package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.wordspec.AnyWordSpec

// testOnly net.yoshinorin.cahsper.http.routes.HomeRouteSpec
class HomeRouteSpec extends AnyWordSpec with ScalatestRouteTest {

  val homeRoute = new HomeRoute()

  "HomeRoute" should {

    "hello Cahsper!!" in {
      Get("/") ~> homeRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`text/plain(UTF-8)`)
        assert(responseAs[String].contains("Hello Cahsper!!"))
      }
    }

    "return robots.txt" in {
      Get("/robots.txt") ~> homeRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`text/plain(UTF-8)`)
        assert(responseAs[String].startsWith("User-agent"))
      }

    }
  }

}
