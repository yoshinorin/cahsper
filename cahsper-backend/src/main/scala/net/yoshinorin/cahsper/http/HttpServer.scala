package net.yoshinorin.cahsper.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import net.yoshinorin.cahsper.http.routes.{ApiStatusRoute, CommentRoute, HomeRoute, UserRoute}

import scala.concurrent.ExecutionContext
import scala.io.StdIn
import scala.util.{Failure, Success}

class HttpServer(
  homeRoute: HomeRoute,
  apiStatusRoute: ApiStatusRoute,
  commentRoute: CommentRoute,
  userRoute: UserRoute
)(implicit actorSystem: ActorSystem, executionContext: ExecutionContext)
    extends HttpLogger {

  import akka.http.scaladsl.server.RouteConcatenation._

  def startServer(host: String, port: Int): Unit = {
    val futureBinding = Http().newServerAt(host, port).bind(routes)

    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        println("Server online at http://{}:{}/", address.getHostString, address.getPort)
        StdIn.readLine()
        futureBinding
          .flatMap(_.unbind())
          .onComplete(_ => actorSystem.terminate())
      case Failure(ex) =>
        println("Failed to bind HTTP endpoint, terminating system", ex)
        actorSystem.terminate()
    }
  }

  def routes: Route = cors() {
    httpLogging {
      homeRoute.route ~
        apiStatusRoute.route ~
        commentRoute.route ~
        userRoute.route
    }
  }
}
