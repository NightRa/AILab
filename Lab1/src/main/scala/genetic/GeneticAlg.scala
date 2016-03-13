package genetic

import java.util.Random

import MateStrategy
import genetic.selection.SelectionStrategy
import genetic.types.{Gene, Population}
import util.JavaUtil
import util.Util._

import scala.annotation.tailrec

class GeneticAlg[A](alg: Genetic[A], mateStrategy: MateStrategy, selection: SelectionStrategy, MaxTimeSecs: Double, rand: Random,
                    initPopulation: => Population[A], initBuffer: => Population[A], show: A => String) {

  def print_best(population: Population[A], i: Int): Unit = {
    val (avg, theStdDev) = stdDev(population.population)((x: Gene[A]) => x.fitness)
    println(f"Best ($i): ${show(population.population(0).gene)}; fitness: ${population.population(0).fitness}; avg: $avg%.3f; stdDev: $theStdDev%.3f")
  }

  def run(print: Boolean): (Population[A], Int) = {
    val timeMili = (1000 * MaxTimeSecs).toLong
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeMili
    val population: Population[A] = initPopulation
    val iterations: Int = goRun(population, initBuffer, 0, endTime, print)
    (population, iterations)
  }

  val epsilon: Double = 1e-12

  @tailrec
  final def goRun(population: Population[A], buffer: Population[A], i: Int, endTime: Long, print: Boolean): Int = {
    if (System.currentTimeMillis() < endTime) {
      population.calc_fitness(alg)
      JavaUtil.sortGenes(population.population)
      if (print) print_best(population, i)
      if (population.population(0).fitness > epsilon) {
        mateStrategy.mateStrategy(alg, selection, population, buffer)
        goRun(buffer, population, i + 1, endTime, print)
      } else i
    } else i
  }

}
