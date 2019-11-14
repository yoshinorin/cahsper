package net.yoshinorin.cahsper.http.auth

import akka.http.scaladsl.server.directives.AuthenticationDirective
import net.yoshinorin.cahsper.models.User

trait Auth {

  def authenticate: AuthenticationDirective[User]

}
