package compute

class Accumulator(val rasterizer: Rasterizer) {

  val pixelArray = Array.ofDim[Int](rasterizer.resolution.x, rasterizer.resolution.y)
  
  def accumulate(point: Point2D): Unit = {
    
    val pixel = rasterizer.toPixelCoordinates(point)
    
    // need to check for 00B
    pixelArray(pixel.x)(pixel.y) += 1
  }
  
}