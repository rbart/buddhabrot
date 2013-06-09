package image

import compute.Accumulator

class EqualMassRenderer(val accumulator: Accumulator) {

  val countIndexMap: Map[Long, Int] = {
    
    println("building count index map")
    // get a sorted list of each unique count
    val uniqueCounts = accumulator.pixelArray.iterator.flatMap(array => array.iterator).toSet.toSeq.sorted
    
    uniqueCounts.zipWithIndex.toMap
  }
  
  def mappedCounts: Array[Array[Double]] = {
    
    println("mapping counts to brightness values")
    
    val brightnessArray = Array.ofDim[Double](accumulator.rasterizer.resolution.x, accumulator.rasterizer.resolution.y)
    
    val pixelArray = accumulator.pixelArray
    
    for (x <- 0 until accumulator.rasterizer.resolution.x) {
      for (y <- 0 until accumulator.rasterizer.resolution.y) {
        
        // get the count at this pixel
        val count = pixelArray(x)(y)
        
        // find this count's index in the sortedCountsUnique
        // we assume it's there - it should be because countIndexMap is built from the same counts.
        val countIndex = countIndexMap(count)
        
        // map this index to a value in interval [0..1]
        val mappedCount = countIndex.toDouble / countIndexMap.size
        
        brightnessArray(x)(y) = mappedCount
      }
    }
    brightnessArray
  }
}