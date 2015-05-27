package org.chronodb






trait Driver {
  def create(name: String, aggregator: String, size: Int, baseResolution: Long,
    backoffFactors: Seq[Int]): Option[Throwable]
  def insert(name: String, timestamp: Long, value: Double): Option[Throwable]
}
