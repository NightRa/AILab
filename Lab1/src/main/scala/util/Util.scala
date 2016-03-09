package util

import java.util.Random

object Util {
  def randString(size: Int, rand: Random): Array[Char] = {
    Array.fill(size)((rand.nextInt(91) + 32).toChar)
  }

  @inline
  def sumBy[A](arr: Array[A])(by: A => Double): Double = {
    arr.foldLeft(0d)((sum, elem) => sum + by(elem))
  }

  @inline
  def avgBy[A](arr: Array[A])(by: A => Double): Double = {
    sumBy(arr)(by) / arr.length
  }

  @inline
  def stdDev[A](arr: Array[A])(by: A => Double): (Double, Double) = {
    val avg = avgBy(arr)(by)
    val stdDev = Math.sqrt(avgBy(arr) { x =>
      val elem = by(x)
      (elem - avg) * (elem - avg)
    })
    (avg, stdDev)
  }
}
