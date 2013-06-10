package main

import scopt.OptionParser

import compute._

import image.EqualMassRenderer
import image.ImageFileWriter

object BuddhabrotCLI {

  def main(args: Array[String]): Unit = {

    // image options
    
    val imgWidth = 600
    val aspectRatio = 4.0 / 3.0
    val oversample = 8
    val imgHeight = (imgWidth / aspectRatio).toInt
    val sampleResolution = IntPair.of(imgWidth * oversample, imgHeight * oversample)
    val pixelResolution = IntPair.of(imgWidth, imgHeight)
    
    // computation options
    
    val imgCenter = Point2D(-0.10, 0)
    val imgDims = DoublePair.of(3.2, 3.2/aspectRatio)
    
    val maxIterations = 5000
    val maxCycle = 500
    
    val imgLowerLeft = Point2D(imgCenter.x - imgDims.x/2, imgCenter.y - imgDims.y/2)
    val imgUpperRight= Point2D(imgCenter.x + imgDims.x/2, imgCenter.y + imgDims.y/2)
    
    val generator = new PointGenerator(imgLowerLeft, imgUpperRight, sampleResolution)
    val rasterizer = new Rasterizer(imgLowerLeft, imgUpperRight, pixelResolution)

    val accumulator = new Accumulator(rasterizer)
    
    // misc options
    
    val reportIntervalMillis = 5000

    new ProgressReporter(generator, reportIntervalMillis).start()

    val millis = Timer.time {
      generator.flatMap { startPoint =>

        new CycleDetector(new MandelbrotIterator(startPoint, maxIterations), maxCycle)

      }.foreach(accumulator.accumulate(_))
    }
    
    println("Rendering time: %.02f sec".format(millis.toDouble / 1000.0))
    
    val renderer = new EqualMassRenderer(accumulator)

    ImageFileWriter.writeToFile("/home/rbart/buddha.bmp", renderer.mappedCounts, pixelResolution)
  }

}