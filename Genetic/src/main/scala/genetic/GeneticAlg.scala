package genetic

import java.util.Random

import genetic.fitnessMapping.FitnessMapping
import genetic.localOptima.LocalOptimaSignal
import genetic.generation.Generation
import genetic.selection.ParentSelection
import genetic.types.{Gene, Population}
import util.JavaUtil
import util.Util._

import scala.annotation.tailrec

class GeneticAlg[A](val genetic: Genetic[A],
                    localOptimaSignal: LocalOptimaSignal[A],
                    normalGeneration: Generation[A],
                    localOptimaGeneration: Generation[A],

                    PopulationSize: Int,
                    rand: Random) {

  val popSize = {
    if (PopulationSize < 3) {
      println("PopulationSize < 3, not ok.")
    }
    PopulationSize max 3
  }

  def print_best(population: Population[A], i: Int): Unit = {
    val (avg, theStdDev) = stdDev(population.population)((x: Gene[A]) => x.fitness)
    println(f"Best ($i): ${genetic.show(population.population(0).gene)}; fitness: ${formatScientific(population.population(0).fitness, 3)}; avg: $avg%.3f; stdDev: $theStdDev%.3f")
  }

  def run(printEvery: Int, MaxTimeSecs: Double): (Population[A], Int) = {
    val timeMili = (1000 * MaxTimeSecs).toLong
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeMili
    val population: Population[A] = new Population[A](Array.fill(popSize)(new Gene(genetic.randomElement(rand), 0)))
    population.calc_fitness(genetic, normalGeneration.fitnessMappings)
    JavaUtil.sortGenes(population.population)

    val initialBuffer = new Population[A](Array.fill(popSize)(new Gene[A](null.asInstanceOf[A], 0)))
    val (bestPop, iterations) = goRun(population, initialBuffer, 0, endTime, printEvery)
    (bestPop, iterations)
  }

  val epsilon: Double = 1e-12

  // printEvery - Every how many iterations to print best in population.
  @tailrec
  final def goRun(population: Population[A], buffer: Population[A], i: Int, endTime: Long, printEvery: Int): (Population[A], Int) = {
    if (System.currentTimeMillis() < endTime) {
      if (printEvery > 0 && i % printEvery == 0) print_best(population, i)
      if (population.population(0).fitness > epsilon) {

        nextGeneration(population, buffer)
        goRun(buffer, population, i + 1, endTime, printEvery)

      } else
        (population, i)
    } else
      (population, i)
  }

  def nextGeneration(population: Population[A], buffer: Population[A]): Unit = {
    val isInLocalOptima = localOptimaSignal.isInLocalOptima(population)
    if (!isInLocalOptima) {
      normalGeneration.nextGeneration(genetic, population, buffer, rand)
    } else {
      localOptimaGeneration.nextGeneration(genetic, population, buffer, rand)
    }
  }

}
