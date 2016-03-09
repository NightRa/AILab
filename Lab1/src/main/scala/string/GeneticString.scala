package string

import java.util.Random

import genetic.Genetic

class GeneticString(heuristic: Array[Char] => Int, rand: Random) extends Genetic[Array[Char]] {
  def fitness(gene: Array[Char]): Double = heuristic(gene)

  @inline
  def mate(x: Array[Char], y: Array[Char]): Array[Char] = {
    StringHeuristics.mate(x, y, rand)
  }

  def mutate(s: Array[Char]): Array[Char] = {
    val i = rand.nextInt(s.length)

    s(i) = (rand.nextInt(91) + 32).toChar

    s
  }
}
