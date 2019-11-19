package net.yoshinorin.cahsper.http.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Route
import net.yoshinorin.cahsper.utils.File

class HomeRoute {

  def route: Route = {
    pathEndOrSingleSlash {
      complete(
        HttpResponse(
          200,
          entity = """
          |Hello Cahsper!!
          |
          |Repository: https://github.com/YoshinoriN/cahsper/
          |API Docs: https://yoshinorin.github.io/cahsper/
          |""".stripMargin
        )
      )
    } ~ path("robots.txt") {
      File.get(System.getProperty("user.dir") + "/src/main/resources/robots.txt") match {
        case Some(x) => getFromFile(x.getAbsolutePath)
        case _ => complete(HttpResponse(404, entity = "Not Found."))
      }
    }
  }

}
