package org.chronodb
package driver



import scala.collection._
import scala.util.Failure
import scala.util.Success
import scala.util.Try



class MemoryWhisperDriver extends Driver {
  import MemoryWhisperDriver._
  
  private val tables = mutable.Map[String, Table]()

  def create(name: String, aggregator: String, size: Int, baseRes: Long,
    backoffFactors: Seq[Int]): Try[Unit] = {
    val result = for {
      _ <- QueryEngine.validateTableName(name)
    } yield {
      tables.get(name) match {
        case Some(table) =>
          Failure(new Exception(s"Table '$name' already exists."))
        case None =>
          Aggregator(aggregator) match {
            case None => Failure(new Exception(s"Invalid aggregator '$aggregator'."))
            case Some(agg) =>
              tables(name) = new Table(name, agg, size, baseRes, backoffFactors)
              Success(())
          }
      }
    }
    result.flatten
  }

  def insert(name: String, timestamp: Long, value: Double): Try[Unit] = {
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
    val backoffFactors: Seq[Int]
  ) {
    val eras = new Array[Era](1 + backoffFactors.length)
    for (i <- 0 to backoffFactors.length)
      eras(i) = new Era(new Array[Long](size), new Array[Double](size))
  }

}
