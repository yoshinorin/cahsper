package net.yoshinorin.cahsper.utils

import java.io.File

object File {

  /**
   * Get file
   *
   * @param path file path
   * @return File
   */
  def get(path: String): Option[File] = {
    Some(new File(path))
  }

}
