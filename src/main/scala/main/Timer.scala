package main

object Timer {

  def time(block: => Unit): Long = {
    
    val startMillis = System.currentTimeMillis()
    
    block
    
    System.currentTimeMillis() - startMillis
  }
  
}