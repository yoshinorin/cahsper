package net.yoshinorin.cahsper.http

import akka.http.scaladsl.model.{ContentTypes, StatusCode, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.WordSpec

// testOnly *HttpServerSpec
class HttpServerSpec extends WordSpec with ScalatestRouteTest {

  val apiStatusRoute = new ApiStatusRoute()

  "ApiStatusRoute" should {

    "return operational JSON" in {
      Get("/status/") ~> apiStatusRoute.route ~> check {
        assert(status == StatusCodes.OK)
        assert(contentType == ContentTypes.`application/json`)
        assert(responseAs[String].replaceAll("\n", "").replaceAll(" ", "") == "{\"status\":\"operational\"}")
      }

    }
  }

}
