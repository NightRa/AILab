package string

import java.util.Random

import genetic.{Metric, Genetic}
import genetic.mating.Crossover
import genetic.mating.Crossover.onePointCrossoverInt

class GeneticString(heuristic: Array[Char] => Double,
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
}
