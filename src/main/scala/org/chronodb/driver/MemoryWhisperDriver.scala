package org.chronodb
package driver



import scala.collection._



class MemoryWhisperDriver extends Driver {
  import MemoryWhisperDriver._
  
  private val tables = mutable.Map[String, Table]()

  def create(name: String, aggregator: String, size: Int, baseResolution: Long,
    backoffFactors: Seq[Int]): Option[Throwable] = {
    ???
  }

  def insert(name: String, timestamp: Long, value: Double): Option[Throwable] = {
    ???
  }
}


object MemoryWhisperDriver {

  class Era(val timestamps: Array[Long], val values: Array[Double])

  class Table(
    val name: String,
    val aggregator: Aggregator,
    val size: Int,
    val baseResolution: Long,
    val backoffFactors: Seq[Long]
  ) {
    val eras = new Array[Era](1 + backoffFactors.length)
    for (i <- 0 to backoffFactors.length)
      eras(i) = new Era(new Array[Long](size), new Array[Double](size))
  }

}
