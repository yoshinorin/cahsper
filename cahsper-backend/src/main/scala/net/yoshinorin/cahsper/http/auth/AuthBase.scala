package net.yoshinorin.cahsper.http.auth

import akka.http.scaladsl.server.directives.AuthenticationDirective

abstract class AuthBase[T] {

  def authenticate: AuthenticationDirective[T]

}
