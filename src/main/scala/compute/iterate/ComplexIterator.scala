package compute.iterate

import compute.Point2D

/**
 * A class that returns a sequence of points on the plane.
 */
abstract class ComplexIterator extends Iterator[Point2D] {

  def hasNext: Boolean
  
  def next(): Point2D
}