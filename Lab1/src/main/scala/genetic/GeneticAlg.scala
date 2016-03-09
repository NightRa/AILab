package genetic

import util.JavaUtil
import util.Util._

import scala.annotation.tailrec

class GeneticAlg[A](alg: Genetic[A], MaxIterations: Int, show: A => String) {

  def print_best(population: Population[A], i: Int): Unit = {
    val (avg, theStdDev) = stdDev(population.population)((x: Gene[A]) => x.fitness)
    println(f"Best ($i): ${show(population.population(0).gene)}; fitness: ${population.population(0).fitness}; avg: $avg%.3f; stdDev: $theStdDev%.3f")
  }

  @tailrec
  final def run(population: Population[A], buffer: Population[A], i: Int = 0): Unit = {
    if (i < MaxIterations) {
      population.calc_fitness(alg)
      JavaUtil.sortGenes(population.population)
      print_best(population, i)
      if (population.population(0).fitness > 0) {
        alg.mateStrategy(population, buffer)
        run(buffer, population, i + 1)
      }
    }
  }

}
