package net.yoshinorin.cahsper

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import net.yoshinorin.cahsper.config.Config
import net.yoshinorin.cahsper.http.{ApiStatusRoute, CommentRoute, HttpServer, UserRoute}
import net.yoshinorin.cahsper.models.db.{CommentRepository, UserRepository}
import net.yoshinorin.cahsper.services.{CommentService, FlywayService, UserService}

import scala.concurrent.ExecutionContextExecutor

object BootStrap extends App {

  FlywayService.migrate()

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val apiStatusRoute: ApiStatusRoute = new ApiStatusRoute()
  val commentRepository: CommentRepository = new CommentRepository()
  val commentService: CommentService = new CommentService(commentRepository)
  val commentServiceRoute: CommentRoute = new CommentRoute(commentService)
  val userRepository: UserRepository = new UserRepository()
  val userService: UserService = new UserService(userRepository)
  val userServiceRoute: UserRoute = new UserRoute(userService)

  val httpServer: HttpServer = new HttpServer(apiStatusRoute, commentServiceRoute, userServiceRoute)

  httpServer.startServer(Config.httpHost, Config.httpPort)

}
