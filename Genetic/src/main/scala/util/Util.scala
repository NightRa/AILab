package util

import java.text.DecimalFormat
import java.util.Random

import scala.reflect.ClassTag

object Util {
  def iteratorIndex[A](iterator: Iterator[A], index: Int): A = iterator.drop(index).next()

  def formatScientific(d: Double, precision: Int): String = {
    val format = new DecimalFormat(s"0.${"#" * precision}E0")
    format.format(d)
  }

  def avgExecutionTime[A](value: => A, rounds: Int): Double = {
    var sum = 0.0
    for (i <- (0 until rounds).par) {
      val (result, _) = timeExecution(value)
      sum += result
    }
    sum / rounds
  }

  // Returns time in seconds.
  def timeExecution[A](value: => A): (Double, A) = {
    val before = System.nanoTime()
    val res = value
    val after = System.nanoTime()
    ((after - before).toDouble / 1e9, res)
  }

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
