package net.yoshinorin.cahsper.auth

import org.scalatest.WordSpec

// testOnly *AwsCognitoSpec
class AwsCognitoSpec extends WordSpec {

  "AwsCognitoSpec" should {

    "return left if JWT is bad" in {
      val jwt =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
      val result = AwsCognito.validateJwt(jwt)

      assert(result.isLeft)
    }

  }

}
