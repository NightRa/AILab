package genetic.params

import genetic.util.JavaUtil
import genetic.util.JavaUtil.formatDouble

import scala.util.Random

// ints: [0, intsMax], doubles: [0,1]
case class Params(ints: Array[Int], doubles: Array[Double]) {
  def prettify: Params = Params(ints, doubles.map(JavaUtil.formatDouble(_, 3).toDouble))
  override def toString: String = "Params: " + ints.mkString("[", ",", "]") + " " + doubles.map(formatDouble(_, 3)).mkString("[", ",", "]")
}

object Params {
  def init(rand: Random, intsMax: Array[Int], intsSize: Int, doublesSize: Int): Params =
    new Params(Array.tabulate(intsSize)(i => rand.nextInt(intsMax(i))), Array.fill(doublesSize)(rand.nextDouble()))
}
