package net.yoshinorin.cahsper

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import net.yoshinorin.cahsper.config.Config
import net.yoshinorin.cahsper.http.{ApiStatusRoute, CommentServiceRoute, HttpServer}
import net.yoshinorin.cahsper.models.db.CommentRepository
import net.yoshinorin.cahsper.services.{CommentService, FlywayService}

import scala.concurrent.ExecutionContextExecutor

object BootStrap extends App {

  FlywayService.migrate()

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val apiStatusRoute: ApiStatusRoute = new ApiStatusRoute()
  val commentRepository: CommentRepository = new CommentRepository()
  val commentService: CommentService = new CommentService(commentRepository)
  val commentServiceRoute: CommentServiceRoute = new CommentServiceRoute(commentService)

  val httpServer: HttpServer = new HttpServer(apiStatusRoute, commentServiceRoute)

  httpServer.startServer(Config.httpHost, Config.httpPort)

}
