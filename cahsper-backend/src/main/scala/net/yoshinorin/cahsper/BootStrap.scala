package net.yoshinorin.cahsper

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import net.yoshinorin.cahsper.config.Config
import net.yoshinorin.cahsper.http.{ApiStatusRoute, HttpServer}
import net.yoshinorin.cahsper.services.FlywayService

import scala.concurrent.ExecutionContextExecutor

object BootStrap extends App {

  FlywayService.migrate()

  implicit val actorSystem: ActorSystem = ActorSystem("cahsper")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContextExecutor: ExecutionContextExecutor = actorSystem.dispatcher

  val apiStatusRoute: ApiStatusRoute = new ApiStatusRoute()
  val httpServer: HttpServer = new HttpServer(apiStatusRoute)

  httpServer.startServer(Config.httpHost, Config.httpPort)

}
