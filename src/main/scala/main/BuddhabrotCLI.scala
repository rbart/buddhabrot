package main

import scopt.OptionParser

import compute._

import image.EqualMassRenderer
import image.ImageFileWriter

object BuddhabrotCLI {

  def main(args: Array[String]): Unit = {

    val sampleResolution = IntPair.of(6400, 6400)
    val pixelResolution = IntPair.of(800, 800)
    val maxIterations = 4096
    val maxCycle = 512
    
    val genll = Point2D(-2, -2)
    val genur = Point2D(2, 2)
    
    val imgll = Point2D(-1.60, -1.60)
    val imgur = Point2D(1.60, 1.60)

    val generator = new PointGenerator(genll, genur, sampleResolution)
    val rasterizer = new Rasterizer(imgll, imgur, pixelResolution)

    val accumulator = new Accumulator(rasterizer)

    new ProgressReporter(generator, 5000).start()

    val millis = Timer.time {
      generator.grouped(1000).flatMap { group => group.par.flatMap { startPoint =>

        new CycleDetector(new MandelbrotIterator(startPoint, maxIterations), maxCycle)

      }}.foreach(accumulator.accumulate(_))
    }
    
    println("Rendering time: %.02f sec".format(millis.toDouble / 1000.0))
    
    val renderer = new EqualMassRenderer(accumulator)

    ImageFileWriter.writeToFile("/home/rbart/buddha.bmp", renderer.mappedCounts, pixelResolution)
  }

}