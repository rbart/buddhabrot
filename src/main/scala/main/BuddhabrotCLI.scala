package main

import scopt.OptionParser

import compute._

import image.EqualMassRenderer
import image.ImageFileWriter

object BuddhabrotCLI {

  def main(args: Array[String]): Unit = {

    val sampleResolution = IntPair.of(1600, 1600)
    val pixelResolution = IntPair.of(800, 800)
    val maxIterations = 500

    val llExtent = Point2D(-2, -2)
    val urExtent = Point2D(2, 2)

    val generator = new PointGenerator(llExtent, urExtent, sampleResolution)
    val rasterizer = new Rasterizer(llExtent, urExtent, pixelResolution)

    val accumulator = new Accumulator(rasterizer)

    val points = generator.points

    points.flatMap({ point =>
      new MandelbrotIterator(point).take(maxIterations)
      //val result = new MandelbrotIterator(point).take(maxIterations).toSeq
      //if (result.length == maxIterations) Seq.empty[Point2D] else result // buddhabrot hack
    }).foreach(accumulator.accumulate(_))

    val renderer = new EqualMassRenderer(accumulator)

    ImageFileWriter.writeToFile("/home/rbart/buddha.bmp", renderer.mappedCounts, pixelResolution)
  }

}