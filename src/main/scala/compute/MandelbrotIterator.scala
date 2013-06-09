package compute

class MandelbrotIterator(val start: Point2D) extends Iterator[Point2D] {

  var prev: Point2D = null // null for perf?
  
  def hasNext = prev == null || (prev.x * prev.x + prev.y * prev.y < 4.0) // !hasNext if it has escaped

  def next: Point2D = {

    if (prev == null) {
      prev = start
      start
    } else {

      val nextX = prev.x * prev.x - prev.y * prev.y
      val nextY = 2 * prev.x * prev.y

      val next = Point2D(start.x + nextX, start.y + nextY)
      prev = next
      next
    }
  }
}

