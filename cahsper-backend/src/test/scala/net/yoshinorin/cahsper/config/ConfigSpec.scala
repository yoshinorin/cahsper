package net.yoshinorin.cahsper.config

import org.scalatest.wordspec.AnyWordSpec

// testOnly net.yoshinorin.cahsper.config.ConfigSpec
class ConfigSpec extends AnyWordSpec {

  "Config" should {
    "database configuration gettable from application.conf" in {
      assert(Config.dbUrl == "jdbc:mariadb://127.0.0.1/cahsper?useUnicode=true&characterEncoding=utf8mb4")
      assert(Config.dbUser == "root")
      assert(Config.dbPassword == "pass")
    }

    "http server configuration gettable from application.conf" in {
      assert(Config.httpHost == "0.0.0.0")
      assert(Config.httpPort == 9001)
    }

    "aws cognito jwk url configuration gettable from application.conf" in {
      assert(Config.awsCognitoJwkUrl.startsWith("https://cognito-idp."))
      assert(Config.awsCognitoJwkUrl.endsWith("/.well-known/jwks.json"))
    }

    "aws cognito jwk iss configuration gettable from application.conf" in {
      assert(Config.awsCognitoJwkIss == "example_user_pool_id")
    }

    "aws cognito application id configuration gettable from application.conf" in {
      assert(Config.awsCognitoAppClientId == "cognito_application_client_id")
    }

  }

}
