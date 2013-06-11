package compute

class Accumulator(val rasterizer: Rasterizer) {

  val pixelArray = Array.ofDim[Long](rasterizer.resolution.x, rasterizer.resolution.y)
  
  def accumulate(point: Point2D): Unit = {
    
    val pixel = rasterizer.toPixelCoordinates(point)
    
    // need to check for 00B
    if (pixel.x >= 0 && pixel.x < rasterizer.resolution.x) {
      if (pixel.y >= 0 && pixel.y < rasterizer.resolution.y) {
        pixelArray(pixel.x)(pixel.y) += 1
      }
    }
  }
  
  def addAll(other: Accumulator): Unit = {
    
    for (x <- 0 until other.pixelArray.length) {
      for (y <- 0 until other.pixelArray(0).length) {
        pixelArray(x)(y) += other.pixelArray(x)(y)
      }
    }
    
    
  }
}