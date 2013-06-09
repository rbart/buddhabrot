package compute

class Rasterizer(val llExtent: Point2D, val urExtent: Point2D, val resolution: IntPair) {

  // The x and y steps: units per pixel
  val xStep = (urExtent.x - llExtent.x) / (resolution.x - 1).toDouble;
  val yStep = (urExtent.y - llExtent.y) / (resolution.y - 1).toDouble;
  
  def toPixelCoordinates(point: Point2D): IntPair = {
    
    // subtract off the llExtent
    val subX = point.x - llExtent.x;
    val subY = point.y - llExtent.y;
    
    // now divide (scale) by xStep and yStep
    val scaleX = subX / xStep
    val scaleY = subY / yStep
    
    // now round to the nearest int. 
    IntPair.from(math.round(scaleX), math.round(scaleY))
  }
}