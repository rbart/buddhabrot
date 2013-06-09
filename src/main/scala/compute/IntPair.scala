package compute

/**
 * Essentially a pixel, but without the screen or rendering context. 
 */

abstract class IntPair {
  
  def x: Int
  def y: Int 
  
  override def equals(other: Any): Boolean = {
    other.isInstanceOf[IntPair] && {
      val otherIntPair = other.asInstanceOf[IntPair]
      otherIntPair.x == x && otherIntPair.y == y
    }
  }
}

object IntPair {
  def of(xval: Int, yval: Int): IntPair = {
    new IntPair() {
      val x = xval;
      val y = yval;
    }
  }
  
  /**
   * Convert down from Long
   */
  def from(xval: Long, yval: Long): IntPair = {
    new IntPair() {
      val x = xval.toInt;
      val y = yval.toInt;
    }
  }
}
