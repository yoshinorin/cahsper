package net.yoshinorin.cahsper.utils

import org.scalatest.wordspec.AnyWordSpec

// testOnly *FileSpec
class FileSpec extends AnyWordSpec {

  "File" should {
    "get file from resources" in {
      val result = File.get(System.getProperty("user.dir") + "/src/test/resources/robots.txt")
      assert(result.get.getAbsoluteFile.getName == "robots.txt")
    }
  }

}
