package genetic.util

import java.lang.Math._

object Distance {
  def square(x: Double): Double = {
    x * x
  }

  def arrayDistanceD(x: Iterator[Double], y: Iterator[Double]): Double = {
    var sum: Double = 0
    while (x.hasNext) {
      val a = x.next
      val b = y.next
      sum += (a - b) * (a - b)
    }
    sqrt(sum)
  }

  def euclidianDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double = {
    sqrt(square(x1 - x2) + square(y1 - y2))
  }

}
