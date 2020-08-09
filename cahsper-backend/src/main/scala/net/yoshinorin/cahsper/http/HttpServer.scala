package net.yoshinorin.cahsper.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.extractClientIP
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import net.yoshinorin.cahsper.http.routes.{ApiStatusRoute, CommentRoute, HomeRoute, UserRoute}

import scala.concurrent.Future

class HttpServer(
  homeRoute: HomeRoute,
  apiStatusRoute: ApiStatusRoute,
  commentRoute: CommentRoute,
  userRoute: UserRoute
)(implicit actorSystem: ActorSystem)
    extends HttpLogger {

  import akka.http.scaladsl.server.RouteConcatenation._

  def startServer(host: String, port: Int): Future[ServerBinding] = {
    Http().newServerAt(host, port).bind(routes)
  }

  def routes: Route =
    cors() {
      extractClientIP { ip =>
        httpLogging(ip) {
          homeRoute.route ~
            apiStatusRoute.route ~
            commentRoute.route ~
            userRoute.route
        }
      }

    }
}
