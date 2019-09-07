package net.yoshinorin.cahsper.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{HttpApp, Route}

class HttpServer(apiStatusRoute: ApiStatusRoute, commentServiceRoute: CommentServiceRoute)(implicit actorSystem: ActorSystem) extends HttpApp {

  override def startServer(host: String, port: Int): Unit = {
    super.startServer(host, port)
    actorSystem.terminate
  }

  override def routes: Route = {
    apiStatusRoute.route ~
      commentServiceRoute.route
  }

}
