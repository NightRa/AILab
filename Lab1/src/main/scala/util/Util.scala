package util

import java.util.Random

import func.Func

import scala.reflect.ClassTag

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

  def zipWith[A, B, C: ClassTag](as: Array[A], bs: Array[B])(f: (A, B) => C): Array[C] = {
    val size = as.length min bs.length
    val arr: Array[C] = Array.ofDim(size)

    for (i <- 0 until size) {
      arr(i) = f(as(i), bs(i))
    }
    arr
  }

  def randAvg(x: Double, y: Double, rand: Random): Double = {
    val w = rand.nextDouble()
    x * w + y * (1 - w)
  }
}
