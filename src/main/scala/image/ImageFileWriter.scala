package image

import compute.IntPair
import java.awt.image.BufferedImage
import java.awt.Color

object ImageFileWriter {

  def writeToFile(filename: String, fileext: String, imgData: Array[Array[Double]], resolution: IntPair): Unit = {
    
    val img = toBufferedImage(imgData, resolution)
    
    javax.imageio.ImageIO.write(img, fileext, new java.io.File(filename + "." + fileext))
  }
  
  def toBufferedImage(imgData: Array[Array[Double]], resolution: IntPair): BufferedImage = {
    
    val img = new BufferedImage(resolution.x, resolution.y, BufferedImage.TYPE_INT_RGB)
    
    for (x <- 0 until resolution.x) {
      for (y <- 0 until resolution.y) {
        
        val cval = imgData(x)(y).toFloat
    	val c = new Color(cval, cval, cval)
        img.setRGB(x, y, c.getRGB())
      }
    }
    
    img
  }
  
}