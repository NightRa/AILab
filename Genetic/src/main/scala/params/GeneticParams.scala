package params

import java.io.PrintWriter
import java.util.Random

import genetic.types.{Gene, Population}
import genetic.{Genetic, GeneticMain, Metric}
import util.Distance
import util.Util._

class GeneticParams(main: GeneticMain[_],
                    IntsMutationSize: Int, DoublesMutationSize: Double, MutationRate: Double, InitialTimeLimit: Double, Rounds: Int,
                    logTimeSec: (Double, Double, Params) => Unit, rand: Random) extends Genetic[Params] {

  var lastAvgTime: Double = InitialTimeLimit

  def currentTimeLimit: Double = 4 * lastAvgTime

  def currentTimeFraction(timeS: Double) = timeS / currentTimeLimit

  def fitnessOnce(gene: Params): Double = {
    val before: Long = System.nanoTime()
    val (population: Population[_], iterations: Int) = main.alg(gene).run(printEvery = 0, currentTimeLimit)
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

  override def metric(): Metric[Params] = new Metric[Params] {
    override def distance(x: Params, y: Params): Double = {
      val doubleDist = Distance.arrayDistanceD(x.doubles, y.doubles)
      val intDist = Distance.arrayDistanceI(x.ints.map(_ / main.intsMax()), y.ints.map(_ / main.intsMax()))
      (intDist + doubleDist) / 2
    }
  }

  override def randomElement(rand: Random): Params = GeneticParams.randomParams(main, rand)

  override def show(gene: Params): String = gene.toString
}

object GeneticParams {
  def randomParams(main: GeneticMain[_], rand: Random): Params = new Params(
    Array.fill(main.intsSize)(rand.nextInt(main.intsMax())),
    Array.fill(main.doublesSize())(rand.nextDouble()))

  val emptyParams: Params = new Params(Array.emptyIntArray, Array.emptyDoubleArray)
  val emptyParamsGene: Gene[Params] = new Gene(emptyParams, 0)
}