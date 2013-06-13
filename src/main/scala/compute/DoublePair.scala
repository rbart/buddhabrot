package compute


abstract class DoublePair {
  def x: Double
  def y: Double
}

object DoublePair {
  
  def of(xval: Double, yval: Double) = {
    new DoublePair { 
      val x = xval
      val y = yval
    }
  }
  
}

case class Point2D(val x: Double, val y: Double) extends DoublePair {
  override def toString: String = {
    "(%.02f, %.02f)".format(x, y)
  }
  
  def invertY: Point2D = Point2D(x, -y)
  def cTc = x*x+y*y
}