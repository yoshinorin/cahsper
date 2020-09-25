package net.yoshinorin.cahsper.http.auth

import akka.http.scaladsl.server.directives.AuthenticationDirective
import net.yoshinorin.cahsper.domains.models.users.UserName

trait Auth {

  def authenticate: AuthenticationDirective[UserName]

}
