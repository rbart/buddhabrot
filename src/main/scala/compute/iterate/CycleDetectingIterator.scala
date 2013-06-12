package compute.iterate

import java.util.ArrayDeque
import java.util.HashSet
import compute.Point2D
import scala.collection.JavaConversions._

/**
 * A wrapper that determines if a cycle is present in the elements of baseIterator
 */
class CycleDetectingIterator(val baseIterator: ComplexIterator, val maxCycle: Int) extends Iterator[Point2D] {
      
  val pointQueue = new ArrayDeque[Point2D](maxCycle)
  val pointSet = new HashSet[Point2D](maxCycle)
  
  def cycleFound = cycleLength != -1
  var cycleLength: Int = -1
  
  def getCycle: Iterator[Point2D] = {
    if (!cycleFound) return Iterator.empty
    pointQueue.take(cycleLength).iterator
  }
  
  def hasNext = baseIterator.hasNext 
  
  def next: Point2D = {
    
    val next = baseIterator.next
    
    // now determine if we've seen this element before
    pointQueue.addFirst(next)
    pointSet.add(next)
    
    val cycleExists = pointQueue.size != pointSet.size
    
    if (cycleExists && !cycleFound) {
      // the duplicate must be later down in the queue - find it's index,
      // that is the length of the cycle.
      cycleLength = pointQueue.iterator.drop(1).indexOf(next) + 1
    }
    
    if (pointQueue.size > maxCycle) {
      val oldPoint = pointQueue.removeLast()
      if (!cycleExists) pointSet.remove(oldPoint)
    }
    
    next
  }
  
}