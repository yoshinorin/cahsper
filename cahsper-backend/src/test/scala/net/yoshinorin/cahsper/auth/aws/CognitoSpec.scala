package net.yoshinorin.cahsper.auth.aws

import net.yoshinorin.cahsper.domains.models.jwt.JwtToken
import org.scalatest.wordspec.AnyWordSpec

// testOnly net.yoshinorin.cahsper.auth.aws.CognitoSpec
class CognitoSpec extends AnyWordSpec {

  "AwsCognitoSpec" should {

    "return left if JWT is bad" in {
      val jwt =
        JwtToken(
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        )
      val result = Cognito.validateJwt(jwt)

      assert(result.isLeft)
    }

  }

}
