package genetic

import java.util.Random

import util.JavaUtil
import util.Util._

import scala.annotation.tailrec

class GeneticAlg[A](alg: Genetic[A], mateStrategy: MateStrategy, MaxIterations: Int, rand: Random, show: A => String) {

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
        mateStrategy.mateStrategy(alg, population, buffer)
        run(buffer, population, i + 1)
      }
    }
  }



}
