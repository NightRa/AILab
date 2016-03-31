package genetic

import java.util.Random

import genetic.localOptima.LocalOptimaSignal
import genetic.mating.MateStrategy
import genetic.selection.SelectionStrategy
import genetic.types.{Gene, Population}
import util.JavaUtil
import util.Util._

import scala.annotation.tailrec

class GeneticAlg[A](alg: Genetic[A], mateStrategy: MateStrategy, selection: SelectionStrategy, localOptimaSignal: LocalOptimaSignal,
                    PopulationSize: Int,
                    MaxTimeSecs: Double, rand: Random,
                    randomElement: Random => A,
                    val show: A => String) {

  val popSize = {
    if (PopulationSize < 3) {
      println("PopulationSize < 3, not ok.")
    }
    PopulationSize max 3
  }

  def print_best(population: Population[A], i: Int): Unit = {
    val (avg, theStdDev) = stdDev(population.population)((x: Gene[A]) => x.fitness)
    println(f"Best ($i): ${show(population.population(0).gene)}; fitness: ${formatScientific(population.population(0).fitness, 3)}; avg: $avg%.3f; stdDev: $theStdDev%.3f")
  }

  def run(printEvery: Int): (Population[A], Int) = {
    val timeMili = (1000 * MaxTimeSecs).toLong
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeMili
    val population: Population[A] = new Population[A](Array.fill(popSize)(new Gene(randomElement(rand), 0)))
    val initialBuffer = new Population[A](Array.fill(popSize)(new Gene[A](null.asInstanceOf[A], 0)))
    val parentsPool: Population[A] = selection.initParentsPool(population.population.length)
    val (bestPop, iterations) = goRun(population, initialBuffer, parentsPool, 0, endTime, printEvery)
    (bestPop, iterations)
  }

  val epsilon: Double = 1e-12

  // printEvery - Every how many iterations to print best in population.
  @tailrec
  final def goRun(population: Population[A], buffer: Population[A], parentsPool: Population[A], i: Int, endTime: Long, printEvery: Int): (Population[A], Int) = {
    if (System.currentTimeMillis() < endTime) {
      population.calc_fitness(alg)
      JavaUtil.sortGenes(population.population)
      if (printEvery > 0 && i % printEvery == 0) print_best(population, i)
      if (population.population(0).fitness > epsilon) {
        val detectedLocalOptima = localOptimaSignal.isInLocalOptima(population)
        mateStrategy.mateStrategy(alg, selection, population, buffer, parentsPool, detectedLocalOptima)
        goRun(buffer, population, parentsPool, i + 1, endTime, printEvery)
      } else
        (population, i)
    } else
      (population, i)
  }

}
