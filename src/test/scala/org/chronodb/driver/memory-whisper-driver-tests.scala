package org.chronodb
package driver



import org.scalatest._
import org.scalatest.Matchers
import scala.util.Failure
import scala.util.Success



class MemoryWhisperDriverSpec extends FunSuite with Matchers {

  test("Must create table") {
    val driver = new MemoryWhisperDriver
    driver.create("table", "avg", 16, 1000, Seq(2)) should equal (Success(()))
  }

  test("Must fail due to bad table name") {
    val driver = new MemoryWhisperDriver
    driver.create("1table", "avg", 16, 1000, Seq(2)) shouldBe a [Failure[_]]
  }

  test("Must fail due to invalid aggregator") {
    val driver = new MemoryWhisperDriver
    driver.create("table", "432rfdas", 16, 1000, Seq(2)) shouldBe a [Failure[_]]
  }

}
