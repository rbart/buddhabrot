package compute


abstract class DoublePair {
  def x: Double
  def y: Double
}

case class Point2D(val x: Double, val y: Double) extends DoublePair