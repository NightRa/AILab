package genetic

import java.util.Random

import util.JavaUtil
import util.Util._

import scala.annotation.tailrec

class GeneticAlg[A](alg: Genetic[A], mateStrategy: MateStrategy, MaxTimeSecs: Double, rand: Random, show: A => String) {

  def print_best(population: Population[A], i: Int): Unit = {
    val (avg, theStdDev) = stdDev(population.population)((x: Gene[A]) => x.fitness)
    println(f"Best ($i): ${show(population.population(0).gene)}; fitness: ${population.population(0).fitness}; avg: $avg%.3f; stdDev: $theStdDev%.3f")
  }

  def run(population: Population[A], buffer: Population[A], print: Boolean): Unit = {
    val timeMili = (1000 * MaxTimeSecs).toLong
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeMili
    goRun(population, buffer, 0, endTime, print)
  }

  @tailrec
  final def goRun(population: Population[A], buffer: Population[A], i: Int, endTime: Long, print: Boolean): Unit = {
    if (System.currentTimeMillis() < endTime) {
      population.calc_fitness(alg)
      JavaUtil.sortGenes(population.population)
      if (print) print_best(population, i)
      if (population.population(0).fitness > 0) {
        mateStrategy.mateStrategy(alg, population, buffer)
        goRun(buffer, population, i + 1, endTime, print)
      }
    }
  }

}
