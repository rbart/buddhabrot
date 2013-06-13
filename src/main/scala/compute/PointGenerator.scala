package compute

class PointGenerator(val llExtent: Point2D, val urExtent: Point2D, val resolution: IntPair) extends Iterator[Point2D] {

  var pointsGenerated: Long = 0
  private val resolutionProduct = (resolution.x.toLong * resolution.y.toLong).toDouble
  def progressFraction = pointsGenerated.toDouble / resolutionProduct
  
  // The x and y steps: units per pixel
  val xStep = (urExtent.x - llExtent.x) / (resolution.x - 1).toDouble;
  val yStep = (urExtent.y - llExtent.y) / (resolution.y - 1).toDouble;
  
  private val backingIterator =  {
    
    val xVals = (llExtent.x to urExtent.x by xStep)
    val yVals = (llExtent.y to urExtent.y by yStep)
    
    xVals.iterator.flatMap { x =>
      yVals.map(y => Point2D(x, y))
    }
  }

  def hasNext = backingIterator.hasNext
  
  def next = {
    pointsGenerated += 1
    backingIterator.next
  }
}