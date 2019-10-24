package net.yoshinorin.cahsper.auth.aws

import org.scalatest.WordSpec

// testOnly *CognitoSpec
class CognitoSpec extends WordSpec {

  "AwsCognitoSpec" should {

    "return left if JWT is bad" in {
      val jwt =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
      val result = Cognito.validateJwt(jwt)

      assert(result.isLeft)
    }

  }

}
