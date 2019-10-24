package net.yoshinorin.cahsper.models.aws.cognito

import net.yoshinorin.cahsper.models.aws.cognito.Jwt.convertJwtClaims
import org.scalatest.WordSpec

// testOnly *JwtSpec
class JwtSpec extends WordSpec {

  "Jwt" should {

    // https://github.com/awslabs/aws-support-tools/tree/master/Cognito/decode-verify-jwt
    "AWS Cognito JWT claims convertible to case class" in {
      val jwtClaims =
        """
          |{
          |  "sub": "aaaaaaaa-bbbb-cccc-dddd-example",
          |  "event_id": "12345-5678-9000",
          |  "token_use": "access",
          |  "scope": "aws.cognito.signin.user",
          |  "auth_time": 1557655406,
          |  "iss": "https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json",
          |  "exp": 1557655406,
          |  "iat": 1557651806,
          |  "jti": "0987654",
          |  "client_id": "clientId12345",
          |  "username": "test1"
          |}
        """.stripMargin.toJwtClaims
      assert(jwtClaims.right.get.isInstanceOf[Jwt])
    }

    "return left if string not convertible to case class" in {
      val notAJson = "{NOT a JSON}".toJwtClaims
      assert(notAJson.isLeft)
    }
  }

}
