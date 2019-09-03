package net.yoshinorin.cahsper

import net.yoshinorin.cahsper.services.FlywayService

object BootStrap extends App {

  FlywayService.migrate()

}
