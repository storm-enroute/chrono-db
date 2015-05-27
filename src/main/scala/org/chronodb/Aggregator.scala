package org.chronodb






trait Aggregator {
  def aggregate(previous: Double, latest: Double): Double
}


object Aggregator {
  def apply(name: String): Option[Aggregator] = name match {
    case "avg" => Some(Avg)
    case "min" => Some(Min)
    case "max" => Some(Max)
    case "last" => Some(Last)
    case "first" => Some(First)
    case _ => None
  }

  object Avg extends Aggregator {
    def aggregate(previous: Double, latest: Double): Double = (previous + latest) / 2
  }

  object Min extends Aggregator {
    def aggregate(previous: Double, latest: Double): Double = math.min(previous, latest)
  }

  object Max extends Aggregator {
    def aggregate(previous: Double, latest: Double): Double = math.max(previous, latest)
  }

  object Last extends Aggregator {
    def aggregate(previous: Double, latest: Double): Double = latest
  }

  object First extends Aggregator {
    def aggregate(previous: Double, latest: Double): Double = previous
  }
}
