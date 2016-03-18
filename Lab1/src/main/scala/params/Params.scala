package params

import scala.util.Random

// ints: [0, intsMax], doubles: [0,1]
case class Params(ints: Array[Int], doubles: Array[Double]) {
  override def toString: String = "Params: " + ints.mkString("[", ",", "]") + " " + doubles.mkString("[", ",", "]")
}

object Params {
  def init(rand:Random, intsMax: Int, intsSize: Int, doublesSize: Int): Params =
    new Params(Array.fill(intsSize)(rand.nextInt(intsMax)), Array.fill(doublesSize)(rand.nextDouble()))
}
