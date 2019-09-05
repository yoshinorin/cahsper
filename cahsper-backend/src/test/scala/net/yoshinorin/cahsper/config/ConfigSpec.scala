package net.yoshinorin.cahsper.config

import org.scalatest.WordSpec

// testOnly *ConfigSpec
class ConfigSpec extends WordSpec {

  "Config" should {
    "gettable database configuration from application.conf" in {
      assert(Config.dbUrl == "jdbc:mariadb://127.0.0.1/cahsper?useUnicode=true&characterEncoding=utf8mb4")
      assert(Config.dbUser == "root")
      assert(Config.dbPassword == "pass")
    }
  }

}
