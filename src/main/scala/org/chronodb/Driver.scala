package org.chronodb



import scala.util.Try



trait Driver {
  def create(name: String, aggregator: String, size: Int, baseResolution: Long,
    backoffFactors: Seq[Int]): Try[Unit]
  def insert(name: String, timestamp: Long, value: Double): Try[Unit]
}
