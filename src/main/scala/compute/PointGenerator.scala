package compute

class PointGenerator(val llExtent: Point2D, val urExtent: Point2D, val resolution: IntPair) {

  // The x and y steps: units per pixel
  val xStep = (urExtent.x - llExtent.x) / (resolution.x - 1).toDouble;
  val yStep = (urExtent.y - llExtent.y) / (resolution.y - 1).toDouble;

  def points: Iterator[Point2D] = {
    
    val xVals = (llExtent.x to urExtent.x by xStep)
    val yVals = (llExtent.y to urExtent.y by yStep)
    
    xVals.iterator.flatMap { x =>
      yVals.map(y => Point2D(x, y))
    }
  }
}