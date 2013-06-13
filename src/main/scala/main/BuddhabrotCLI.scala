package main

import scopt.OptionParser
import java.io.File
import compute.IntPair
import compute.Point2D
import compute.DoublePair
import compute.iterate.CycleDetectingIterator
import image.EqualMassRenderer
import image.ImageFileWriter
import compute.iterate.MandelbrotIterator
import compute.PointGenerator
import compute.Accumulator
import compute.Rasterizer
import scala.collection.JavaConversions._

case class Config(
  pixelWidth: Int = 800 * 1,
  pixelHeight: Int = 600 * 1,
  oversample: Int = 4,
  imgCenterX: Double = -0.20,
  imgCenterY: Double = 0.0,
  imgWidth: Double = 5.0,
  maxIterations: Int = 2500,
  maxCycle: Int = 100,
  filename: String = "./default",
  fileExt: String = "bmp") {

  val aspectRatio = pixelWidth.toDouble / pixelHeight.toDouble
  val sampleResolution = IntPair.of(pixelWidth * oversample, pixelHeight * oversample)
  val pixelResolution = IntPair.of(pixelWidth, pixelHeight)
  val imgHeight = imgWidth / aspectRatio
  val imgCenter = Point2D(imgCenterX, imgCenterY)
  val imgDims = DoublePair.of(imgWidth, imgHeight)

  val imgLowerLeft = Point2D(imgCenter.x - imgDims.x / 2, imgCenter.y - imgDims.y / 2)
  val imgUpperRight = Point2D(imgCenter.x + imgDims.x / 2, imgCenter.y + imgDims.y / 2)
}

object Primes {
  // omit 2
  val set = Set(2, 3, 5,  7,
      11, 13 , 17 , 19 , 23 , 29 , 31 , 37 , 41 , 43 , 47 , 53 , 59 , 61 , 67, 71, 73 , 79 , 83 , 89 , 97 , 101 , 103 , 107 , 109 , 113 , 127 , 131 , 137 , 139 , 149 , 151 , 157 , 163,
167 , 173 , 179 , 181 , 191 , 193 , 197 , 199 , 211 , 223 , 227 , 229 , 233 , 239 , 241 , 251 , 257 , 263 , 269,
271 , 277 , 281 , 283 , 293 , 307 , 311 , 313 , 317 , 331 , 337 , 347 , 349 , 353 , 359 , 367 , 373 , 379 , 383,
389 , 397 , 401 , 409 , 419 , 421 , 431 , 433 , 439 , 443 , 449 , 457 , 461 , 463 , 467 , 479 , 487 , 491 , 499)
  
}

object BuddhabrotCLI {

  def main(args: Array[String]): Unit = {

    // image options

    val parser = new OptionParser[Config]("Buddhabrot CLI options") {

      this.opt[Int]("pixelWidth") action { 
        (width, config) => config.copy(pixelWidth = width) 
      } text ("The width of the rendered image in pixels")
      this.opt[Int]("pixelHeight") action { 
        (height, config) => config.copy(pixelHeight = height)
      } text ("The height of the rendered image in pixels")
      this.opt[Int]("oversample") action { 
        (os, config) => config.copy(oversample = os)
      } text ("The factor to multiply the pixel resolution by when sampling the plane")
      this.opt[Double]("imgCenterX") action { 
        (icx, config) => config.copy(imgCenterX = icx)
      } text ("The X-coordinate of the center of the image")
      this.opt[Double]("imgCenterY") action { 
        (icy, config) => config.copy(imgCenterY = icy)
      } text ("The Y-coordinate of the center of the image")
      this.opt[Double]("imgWidth") action { 
        (iw, config) => config.copy(imgWidth = iw)
      } text ("The width of the image in the plane")
      this.opt[Int]("maxIterations") action { 
        (maxiters, config) => config.copy(maxIterations = maxiters)
      } text ("Max iterations of the fractal formula")
      this.opt[Int]("maxCycle") action { 
        (mc, config) => config.copy(maxCycle = mc)
      } text ("Max cycle length detectable (for stopping iteration once a cycle is detected")
      this.opt[String]("filename") action { 
        (outFile, config) => config.copy(filename = outFile)
      } text ("The output name (without extension")
      this.opt[String]("fileExt") action { 
        (ext, config) => config.copy(fileExt = ext)
      } text ("The output file extension")
    }

    parser.parse(args, Config()) map { config =>
      renderImage(config)
    }
  }

  def renderImage(c: Config): Unit = {

    val generator = new PointGenerator(c.imgLowerLeft, c.imgUpperRight, c.sampleResolution)
    val rasterizer = new Rasterizer(c.imgLowerLeft, c.imgUpperRight, c.pixelResolution)

    val accumulator = new Accumulator(rasterizer)

    // misc options

    val reportIntervalMillis = 5000

    // Processing begins here

    new ProgressReporter(generator, reportIntervalMillis).start()

    val millis = Timer.time {
      
      val filterHack = generator.filter(_.y < 0)
      
      filterHack.grouped(20000).grouped(100).foreach { bigGroup =>

        bigGroup.par.foreach { smallGroup =>
          smallGroup.foreach { startPoint =>

            val baseMbrotIterator = new MandelbrotIterator(startPoint, c.maxIterations)
            val cycleDetIterator = new CycleDetectingIterator(baseMbrotIterator, c.maxCycle)

            cycleDetIterator.takeWhile { _ => !cycleDetIterator.cycleFound } foreach {
              point =>
                accumulator.accumulate(point)
                accumulator.accumulate(point.invertY)
            }
          }
        }

      } 
      // End timer
    }

    println("Rendering time: %.02f sec".format(millis.toDouble / 1000.0))
    val renderer = new EqualMassRenderer(accumulator)
    ImageFileWriter.writeToFile(c.filename, c.fileExt, renderer.mappedCounts, c.pixelResolution)
  }

}