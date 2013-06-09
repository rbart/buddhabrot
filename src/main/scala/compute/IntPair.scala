package compute

/**
 * Essentially a pixel, but without the screen or rendering context. 
 */

abstract class IntPair {
  
  def x: Int
  def y: Int
}

object IntPair {
  def of(xval: Int, yval: Int): IntPair = {
    new IntPair() {
      val x = xval;
      val y = yval;
    }
  }
}
