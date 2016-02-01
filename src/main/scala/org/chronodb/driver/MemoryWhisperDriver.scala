package org.chronodb
package driver



import scala.collection._
import scala.util.Failure
import scala.util.Success
import scala.util.Try



class MemoryWhisperDriver extends Driver {
  import MemoryWhisperDriver._
  
  private val lock = new AnyRef
  private val tables = mutable.Map[String, Table]()
  private val success = Success(())

  def create(name: String, aggregator: String, size: Int, baseRes: Long,
    backoffFactors: Seq[Int]): Try[Unit] = {
    val props = List(
      "aggregator" -> aggregator,
      "size" -> size,
      "base-resolution" -> baseRes,
      "backoff-factors" -> backoffFactors
    )
    create(name, props: _*)
  }

  def create(name: String, props: (String, Any)*): Try[Unit] = lock.synchronized {
    val propmap = props.toMap
    val aggregator = propmap("aggregator").asInstanceOf[String]
    val size = propmap("size").asInstanceOf[Int]
    val baseRes = propmap("base-resolution").asInstanceOf[Long]
    val backoffFactors = propmap("backoff-factors").asInstanceOf[Seq[Int]]

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
    tables.get(name) match {
      case Some(table) =>
        ???
      case None =>
        Failure(new Exception(s"Table '$name' does not exist."))
    }
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
