package params

import java.io.PrintWriter
import java.util.Random

import genetic.types.Population
import genetic.{Genetic, GeneticMain}
import util.Util._

class GeneticParams(main: GeneticMain[_],
                    IntsMutationSize: Int, DoublesMutationSize: Double, MutationRate: Double, InitialTimeLimit: Double, Rounds: Int,
                    logTimeSec: (Double, Double, Params) => Unit, rand: Random) extends Genetic[Params] {

  var lastAvgTime: Double = InitialTimeLimit

  def currentTimeLimit: Double = 4 * lastAvgTime

  def currentTimeFraction(timeS: Double) = timeS / currentTimeLimit

  def fitnessOnce(gene: Params): Double = {
    val before: Long = System.nanoTime()
    val (population: Population[_], iterations: Int) = main.alg(gene, currentTimeLimit).run(printEvery = 0)
    val after: Long = System.nanoTime()
    val time: Long = after - before

    val timeS = time.toDouble / 1e9
    val timeFraction = currentTimeFraction(timeS)

    logTimeSec(timeS, timeFraction, gene)

    if (timeFraction > 0.99) 1
    // Finished => [0,0.5], Not finished => [0.5,1]
    else 0.5 * timeFraction + 0.5 * population.population.minBy(_.fitness).fitness
  }

  def fitness(gene: Params): Double = {
    var sum = 0.0
    for (i <- 0 until Rounds) {
      val result = fitnessOnce(gene)
      if (result == 1) return 1
      else sum += result
    }
    val avgTime = sum / Rounds
    lastAvgTime = lastAvgTime min avgTime
    avgTime
  }

  def mate(a: Params, b: Params): Params = {
    val ints = zipWith(a.ints, b.ints)((x, y) => randAvg(x, y, rand).toInt)
    val doubles = zipWith(a.doubles, b.doubles)((x, y) => randAvg(x, y, rand))
    new Params(ints, doubles)
  }

  def mutate(params: Params): Params = {
    for (i <- params.ints.indices) {
      if (rand.nextDouble() < MutationRate) {
        val delta = (rand.nextInt(IntsMutationSize) - (IntsMutationSize / 2)) * 2
        // max 2 so that population size / 2 isn't 0.
        params.ints(i) = (params.ints(i) + delta) max 3 min main.intsMax
      }
    }

    for (i <- params.doubles.indices) {
      if (rand.nextDouble() < MutationRate) {
        val delta = ((rand.nextDouble() * DoublesMutationSize) - (DoublesMutationSize / 2)) * 2
        params.doubles(i) = (params.doubles(i) + delta) max 0 min 1
      }
    }

    params
  }
}
