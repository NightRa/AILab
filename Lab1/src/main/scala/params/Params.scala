package params

import scala.util.Random

// ints: [0, intsMax], doubles: [0,1]
class Params(val ints: Array[Int], val doubles: Array[Double]) {
  override def toString: String = "Params: " + ints.mkString("[", ",", "]") + " " + doubles.mkString("[", ",", "]")
}

object Params {
  def init(rand:Random, intsMax: Int, intsSize: Int, doublesSize: Int): Params =
    new Params(Array.fill(intsSize)(rand.nextInt(intsMax)), Array.fill(doublesSize)(rand.nextDouble()))

  def apply(ints: Int*)(doubles: Double*): Params = new Params(ints.toArray, doubles.toArray)
}
