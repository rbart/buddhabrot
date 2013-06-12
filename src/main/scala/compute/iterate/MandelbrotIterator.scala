package compute.iterate

import compute.Point2D

/**
 * A simple iterator that iteratively computes z=z^2+c starting with z=c
 */
class MandelbrotIterator(val start: Point2D, val maxIterations: Int) extends ComplexIterator {

  private var iterations = 0
  
  private var prev: Point2D = null // null instead of option for perf (?)
  
  def hasNext = (iterations < maxIterations) && !hasEscaped

  private def hasEscaped = (prev != null && (prev.x * prev.x + prev.y * prev.y > 4.0))
  
  def next: Point2D = {

    iterations += 1
    
    if (prev == null) {
      prev = start
      start
    } else {

      // compute mandelbrot iteration: z^2+c
      val nextX = prev.x * prev.x - prev.y * prev.y
      val nextY = 2 * prev.x * prev.y

      val next = Point2D(start.x + nextX, start.y + nextY)
      prev = next
      next
    }
  }
}

