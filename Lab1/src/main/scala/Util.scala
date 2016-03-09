import java.util.Random

object Util {
  def randString(size: Int, rand: Random): Array[Char] = {
    Array.fill(size)((rand.nextInt(91) + 32).toChar)
  }

  def sortBy[A, B](arr: Array[A])(f: A => B)(implicit ordering: Ordering[B]): Unit = {
    // TODO
    // JavaUtil.sortBy(arr, f, ordering)
    System.arraycopy(arr.sortBy(f), 0, arr, 0, arr.length)
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
