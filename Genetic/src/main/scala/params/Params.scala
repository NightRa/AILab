package params

import util.JavaUtil.formatDouble

import scala.util.Random

// ints: [0, intsMax], doubles: [0,1]
case class Params(ints: Array[Int], doubles: Array[Double]) {
  override def toString: String = "Params: " + ints.mkString("[", ",", "]") + " " + doubles.map(formatDouble(_, 3)).mkString("[", ",", "]")
}

object Params {
  def init(rand:Random, intsMax: Array[Int], intsSize: Int, doublesSize: Int): Params =
    new Params(Array.tabulate(intsSize)(i => rand.nextInt(intsMax(i))), Array.fill(doublesSize)(rand.nextDouble()))
}
