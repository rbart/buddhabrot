package compute

import org.scalatest.FlatSpec

class PointGeneratorTest extends FlatSpec {

  "PointGenerator" should "enumerate points in a simple grid" in {
    
    val origin = Point2D(0, 0)
    val extent = Point2D(1, 1)
    val resolution = IntPair.of(2, 2)
    
    val generator = new PointGenerator(origin, extent, resolution)
    val points = generator.points.toSeq
    assert(points.size == 4)
    assert(points.contains(origin))
    assert(points.contains(Point2D(0, 1)))
    assert(points.contains(Point2D(1, 0)))
    assert(points.contains(extent))
  }
 
  it should "enumerate points in a 3x3 grid" in {
    
    val origin = Point2D(0, 0)
    val extent = Point2D(2, 2)
    val resolution = IntPair.of(3, 3)
    
    val generator = new PointGenerator(origin, extent, resolution)
    val points = generator.points.toSeq
    assert(points.size == 9)
    assert(points.contains(origin))
    assert(points.contains(Point2D(0, 1)))
    assert(points.contains(Point2D(0, 2)))
    assert(points.contains(Point2D(1, 0)))
    assert(points.contains(Point2D(1, 1)))
    assert(points.contains(Point2D(1, 2)))
    assert(points.contains(Point2D(2, 0)))
    assert(points.contains(Point2D(2, 1)))
    assert(points.contains(Point2D(2, 2))) 
  }
}