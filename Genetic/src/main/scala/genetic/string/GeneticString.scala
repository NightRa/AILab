package genetic.string

import java.util.Random

import genetic.genetic.Metric
import genetic.util.Util
import java.util

import genetic.genetic.{Genetic, Metric}

class GeneticString(targetString: Array[Char],
                    heuristic: Array[Char] => Double,
                    crossover: (Array[Char], Array[Char], Random) => Array[Char],
                    rand: Random) extends Genetic[Array[Char]] {
  def fitness(gene: Array[Char]): Double = heuristic(gene)

  @inline
  def mate(x: Array[Char], y: Array[Char]): Array[Char] = {
    crossover(x, y, rand)
  }

  def mutate(s: Array[Char]): Array[Char] = {
    val i = rand.nextInt(s.length)

    s(i) = (rand.nextInt(91) + 32).toChar

    s
  }

  override def metric(): Metric[Array[Char]] = new Metric[Array[Char]] {
    override def distance(x: Array[Char], y: Array[Char]): Double = {
      StringHeuristics.heuristic2(x, y)
    }
  }

  override def randomElement(rand: Random): Array[Char] = Util.randString(targetString.length, rand)

  override def show(charArr: Array[Char]): String = charArr.mkString

  override def showScientific(): Boolean = false

  override def hash(gene: Array[Char]): Int = util.Arrays.hashCode(gene)
}
