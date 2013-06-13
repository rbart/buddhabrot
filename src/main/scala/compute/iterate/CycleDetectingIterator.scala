package compute.iterate

import java.util.ArrayDeque
import java.util.HashMap
import compute.Point2D
import scala.collection.JavaConversions._

/**
 * A wrapper that determines if a cycle is present in the elements of baseIterator
 */
class CycleDetectingIterator(val baseIterator: ComplexIterator, val maxCycle: Int) extends Iterator[Point2D] {
      
  private var time: Long = 0
 
  val pointToTime = new HashMap[Point2D, Long](maxCycle)
  val timeToPoint = new HashMap[Long, Point2D](maxCycle)
  
  def cycleFound = cycleLength != -1
  var cycleLength: Int = -1
  
  def getCycle: Iterator[Point2D] = {
    if (!cycleFound) return Iterator.empty
    (time to time - cycleLength + 1 by -1).iterator map timeToPoint.get
  }
  
  def hasNext = baseIterator.hasNext 
  
  def next: Point2D = {
    
    time += 1
    
    val next = baseIterator.next
    
    // now determine if we've seen this element before
    val cycleExists = pointToTime.containsKey(next)
    
    if (cycleExists) {
    	// if we have, then update cycleLength accordingly
      val timeLastSeen = pointToTime.get(next)
      cycleLength = (time - timeLastSeen).toInt + 1
    }
    
    pointToTime.put(next, time)
    timeToPoint.put(time, next)
    
    val delTime = time - maxCycle
    if (delTime >= 0) {
      val deleted = timeToPoint.remove(delTime)
      if (!next.equals(deleted)) pointToTime.remove(deleted)
    }
    
    next
  }
  
}