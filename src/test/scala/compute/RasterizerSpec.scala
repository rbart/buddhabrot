package compute

import org.scalatest.FlatSpec

class RasterizerSpec extends FlatSpec {

  "Rasterizer" should "convert points on the plane to appropriate pixel locations" in {
    
    val origin = Point2D(-1, -2)
    val extent = Point2D(3, 2)
    val resolution = IntPair.of(5, 5)
    
    val rasterizer = new Rasterizer(origin, extent, resolution)
    
    assert(rasterizer.toPixelCoordinates(origin).equals(IntPair.of(0,0)))
    assert(rasterizer.toPixelCoordinates(extent).equals(IntPair.of(4,4)))
    assert(rasterizer.toPixelCoordinates(Point2D(0,0)).equals(IntPair.of(1,2)))
    
  }
  
}