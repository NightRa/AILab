package genetic.genetic

import java.util.Random

import genetic.genetic.types.{Gene, Population}
import genetic.util.JavaUtil
import genetic.util.Util._

import scala.annotation.tailrec

object GeneticAlg {
  val epsilon: Double = 1e-12
}

class GeneticAlg[A](val genetic: Genetic[A],
                    geneticEngine: GeneticEngine,
                    rand: Random) {

  import geneticEngine._

  var lastStartTime: Long = System.currentTimeMillis()

  val popSize = {
    if (PopulationSize < 3) {
      println("PopulationSize < 3, not ok.")
    }
    PopulationSize max 3
  }

  def print_best(population: Population[A], i: Int, lastWasLocalOptimum: Boolean): Unit = {
    val generationTime = System.currentTimeMillis() - lastStartTime
    val (avg, theStdDev) = stdDev(population.population)((x: Gene[A]) => x.fitness)
    val fitness: Double = population.population(0).fitness
    val meaningfulFitness: Double = genetic.score(population.population(0))
    val showFitness =
      if(genetic.showScientific())
        formatScientific(fitness, 3)
      else
        JavaUtil.formatDouble(fitness, 4)

    val showMeaningfulFitness =
      if(genetic.showScientific())
        formatScientific(meaningfulFitness, 3)
      else
        JavaUtil.formatDouble(meaningfulFitness, 4)

    println(f"Best ($i): ${genetic.show(population.population(0).gene)}; local optimum: $lastWasLocalOptimum, fitness: $showFitness; meaningful: $showMeaningfulFitness, time: $generationTime ms; avg: $avg%.3f; stdDev: $theStdDev%.3f")
  }

  def run(printEvery: Int, MaxTimeSecs: Double): (Population[A], Int) = {
    val timeMili = (1000 * MaxTimeSecs).toLong
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeMili
    val population: Population[A] = new Population[A](Array.fill(popSize)(new Gene(genetic.randomElement(rand), 0)))
    population.calc_fitness(genetic, normalGeneration.fitnessMappings)
    JavaUtil.sortGenes(population.population)

    val initialBuffer = new Population[A](Array.fill(popSize)(new Gene[A](null.asInstanceOf[A], 0)))
    val (bestPop, iterations) = goRun(population, initialBuffer, 0, lastWasLocalOptimum = false, endTime, printEvery)
    (bestPop, iterations)
  }

  // printEvery - Every how many iterations to print best in population.
  @tailrec
  final def goRun(population: Population[A], buffer: Population[A], i: Int, lastWasLocalOptimum: Boolean, endTime: Long, printEvery: Int): (Population[A], Int) = {
    if (printEvery > 0 && i % printEvery == 0) print_best(population, i, lastWasLocalOptimum)

    val generationStartTime = System.currentTimeMillis()
    if (generationStartTime < endTime) {
      lastStartTime = generationStartTime
      if (population.population(0).fitness > GeneticAlg.epsilon) {

        val isInLocalOptimum = nextGeneration(population, buffer)
        goRun(buffer, population, i + 1, isInLocalOptimum, endTime, printEvery)

      } else
        (population, i)
    } else
      (population, i)
  }

  def nextGeneration(population: Population[A], buffer: Population[A]): Boolean = {
    val isInLocalOptima = localOptimaSignal.isInLocalOptima(genetic.metric(), population)
    if (!isInLocalOptima) {
      normalGeneration.nextGeneration(genetic, population, buffer, rand)
    } else {
      localOptimaGeneration.nextGeneration(genetic, population, buffer, rand)
    }
    isInLocalOptima
  }

}
