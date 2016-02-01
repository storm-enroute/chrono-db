package org.chronodb



import scala.util.Try



trait Driver {
  def create(name: String, props: (String, Any)*): Try[Unit]
  def insert(name: String, timestamp: Long, value: Double): Try[Unit]
}
