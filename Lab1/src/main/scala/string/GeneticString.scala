package string

import java.util.Random

import genetic.{Genetic, Population}

import scala.annotation.tailrec

class GeneticString(heuristic: Array[Char] => Int, ElitismRate: Float, MutationRate: Float, rand: Random) extends Genetic[Array[Char]] {
  def fitness(gene: Array[Char]): Int = heuristic(gene)

  @tailrec
  final def elitism(population: Population[Array[Char]], buffer: Population[Array[Char]], elitismAmount: Int): Unit = {
    if (elitismAmount > 0) {
      buffer.population(elitismAmount - 1) = population.population(elitismAmount - 1)
      elitism(population, buffer, elitismAmount - 1)
    }
  }

  def mateStrategy(population: Population[Array[Char]], buffer: Population[Array[Char]]): Unit = {
    val popSize = population.population.length
    val elites = (popSize * ElitismRate).toInt
    elitism(population, buffer, elites)
    @tailrec
    def go(i: Int): Unit = {
      if (i < popSize) {
        val i1 = rand.nextInt(popSize / 2)
        val i2 = rand.nextInt(popSize / 2)

        buffer.population(i).gene = mate(population.population(i1).gene, population.population(i2).gene)

        if (rand.nextFloat() < MutationRate)
          buffer.population(i).gene = mutate(buffer.population(i).gene)
        go(i + 1)
      }
    }
    go(elites)
  }

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
