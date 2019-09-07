package net.yoshinorin.cahsper.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes._

class ApiStatusRoute(implicit actorSystem: ActorSystem) {

  def route: Route = {
    pathPrefix("status") {
      pathEndOrSingleSlash {
        get {
          complete(HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, "{\"status\":\"operational\"}")))
        }
      }
    }
  }

}
