package main

import scopt.OptionParser
import java.io.File
import compute._

import image.EqualMassRenderer
import image.ImageFileWriter

case class Config(
  pixelWidth: Int = 600,
  pixelHeight: Int = 400,
  oversample: Int = 4,
  imgCenterX: Double = -0.20,
  imgCenterY: Double = 0.0,
  imgWidth: Double = 5.0,
  maxIterations: Int = 500,
  maxCycle: Int = 20,
  filename: String = "./default_fractal",
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
      generator.grouped(20000).grouped(100).foreach { bigGroup =>

        bigGroup.par.foreach { smallGroup =>
          smallGroup.foreach { startPoint =>
            new CycleDetector(new MandelbrotIterator(startPoint, c.maxIterations), c.maxCycle).foreach { resultPoint =>
              accumulator.accumulate(resultPoint)
            }
          }
        }

      }
    }

    println("Rendering time: %.02f sec".format(millis.toDouble / 1000.0))
    val renderer = new EqualMassRenderer(accumulator)
    ImageFileWriter.writeToFile(c.filename, c.fileExt, renderer.mappedCounts, c.pixelResolution)
  }

}