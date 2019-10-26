package net.yoshinorin.cahsper.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{HttpApp, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import net.yoshinorin.cahsper.http.routes.{ApiStatusRoute, CommentRoute, UserRoute}

class HttpServer(
  apiStatusRoute: ApiStatusRoute,
  commentRoute: CommentRoute,
  userRoute: UserRoute
)(implicit actorSystem: ActorSystem)
    extends HttpApp
    with HttpLogger {

  override def startServer(host: String, port: Int): Unit = {
    super.startServer(host, port)
    actorSystem.terminate
  }

  override def routes: Route = cors() {
    httpLogging {
      apiStatusRoute.route ~
        commentRoute.route ~
        userRoute.route
    }
  }
}
