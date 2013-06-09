package compute

class MandelbrotIterator(private var current: Point2D) extends Iterator[Point2D] {

  def hasNext = true
  
  def next: Point2D = {
    
    val nextX = current.x * current.x - current.y * current.y
    val nextY = 2 * current.x * current.y
    
    Point2D(current.x + nextX, current.y + nextY)
  }
}