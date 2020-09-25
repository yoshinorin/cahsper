package net.yoshinorin.cahsper

import akka.actor.ActorSystem
import net.yoshinorin.cahsper.config.Config
import net.yoshinorin.cahsper.domains.users.UserRepository
import net.yoshinorin.cahsper.http.routes.{ApiStatusRoute, CommentRoute, HomeRoute, UserRoute}
import net.yoshinorin.cahsper.http.HttpServer
import net.yoshinorin.cahsper.infrastructure.Migration
import net.yoshinorin.cahsper.models.db.CommentRepository
import net.yoshinorin.cahsper.services.{CommentService, UserService}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object BootStrap extends App {

  Migration.migrate()

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val auth = new http.auth.Cognito()

  val homeRoute: HomeRoute = new HomeRoute()
  val apiStatusRoute: ApiStatusRoute = new ApiStatusRoute()

  val commentRepository: CommentRepository = new CommentRepository()
  val commentService: CommentService = new CommentService(commentRepository)
  val commentServiceRoute: CommentRoute = new CommentRoute(commentService)

  val userRepository: UserRepository = new UserRepository()
  val userService: UserService = new UserService(userRepository)
  val userServiceRoute: UserRoute = new UserRoute(auth, userService, commentService)

  val httpServer: HttpServer = new HttpServer(homeRoute, apiStatusRoute, commentServiceRoute, userServiceRoute)

  httpServer.startServer(Config.httpHost, Config.httpPort).onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      println(s"Server online at http://${address.getHostString}:${address.getPort}/")

    // Cahsper exit with 0 when run on the docker.
    // TODO: exit with press key for development
    /*
      StdIn.readLine()
      binding
        .unbind()
        .onComplete(_ => actorSystem.terminate())

     */
    case Failure(ex) =>
      println("Failed to bind HTTP endpoint, terminating system", ex)
      actorSystem.terminate()
  }

}
