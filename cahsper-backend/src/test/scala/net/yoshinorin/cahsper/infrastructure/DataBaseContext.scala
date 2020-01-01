package net.yoshinorin.cahsper.infrastructure

import net.yoshinorin.cahsper.infrastructure.OrderType.OrderConverter
import org.scalatest.wordspec.AnyWordSpec

// testOnly *DataBaseContextSpec
class DataBaseContext extends AnyWordSpec {

  "DataBaseContext" should {

    "string asc return Order.ASC" in {
      assert("asc".toOrder == OrderType.ASC)
    }

    "string desc return Order.DESC" in {
      assert("desc".toOrder == OrderType.DESC)
    }

    "other strings return Order.DESC" in {
      assert("other".toOrder == OrderType.DESC)
    }

  }

}
