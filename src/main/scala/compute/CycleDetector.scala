package compute

import java.util.ArrayDeque
import java.util.HashSet

/**
 * A wrapper that terminates a base iterator if a cycle is detected.
 */
class CycleDetector(val baseIterator: Iterator[Point2D], val maxCycle: Int) extends Iterator[Point2D] {

  private val pointQueue = new ArrayDeque[Point2D](maxCycle)
  private val pointSet = new HashSet[Point2D](maxCycle)
  
  private var cycleFound = false
  
  def hasNext = !cycleFound && baseIterator.hasNext 
  
  def next: Point2D = {
    
    val next = baseIterator.next
    
    // now determine if we've seen this element before
    pointQueue.addFirst(next)
    pointSet.add(next)
    
    val cycleExists = pointQueue.size != pointSet.size
    
    if (cycleExists) cycleFound = true
    
    if (pointQueue.size > maxCycle) {
      val oldPoint = pointQueue.removeLast()
      if (!cycleExists) pointSet.remove(oldPoint)
    }
    
    next
  }
  
}