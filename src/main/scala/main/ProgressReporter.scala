package main

import compute.PointGenerator

class ProgressReporter(val inputGenerator: PointGenerator, val intervalMillis: Int) extends Thread {


  
  override def run(): Unit = {
    
    var lastReport = System.currentTimeMillis()
    var done = false
    
    while (!done) {
      
      // sleep for the remaining time
      val timeSinceLast = System.currentTimeMillis() - lastReport
      val timeRemaining = intervalMillis - timeSinceLast
      
      try { 
        if (timeRemaining > 0) Thread.sleep(timeRemaining)
      } catch {
        case e: InterruptedException => { }
      }
      
      if (inputGenerator.progressFraction > 0.99) done = true
      
      println("%.0f%%".format(inputGenerator.progressFraction * 100.0))
      
      lastReport = System.currentTimeMillis()
    }
    
  }
  

  
}