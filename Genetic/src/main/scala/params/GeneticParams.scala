package params

import java.util.Random

import genetic.types.{Gene, Population}
import genetic.{Genetic, GeneticAlg, Metric}
import parametric.Parametric
import util.Distance
import util.Util._

class GeneticParams(geneticParam: Parametric[GeneticAlg[_]],
                    IntsMutationSize: Double,
                    DoublesMutationSize: Double,
                    MutationRate: Double,
                    InitialTimeLimit: Double,
                    Rounds: Int,
                    pressure: Double,
                    relief: Double,
                    logTimeSec: (Double, Double, Params) => Unit, rand: Random) extends Genetic[Params] {

  var lastAvgTime: Double = InitialTimeLimit

  def currentTimeLimit: Double = Math.min((pressure * 10) * lastAvgTime, InitialTimeLimit)

  def currentTimeFraction(timeS: Double) = timeS / currentTimeLimit

  def fitnessOnce(gene: Params): Double = {
    val before: Long = System.nanoTime()
    val (population: Population[_], iterations: Int) = geneticParam.applyArrayParams(gene).run(printEvery = 0, currentTimeLimit)
    val after: Long = System.nanoTime()
    val time: Long = after - before

    val timeS = time.toDouble / 1e9
    val timeFraction = currentTimeFraction(timeS)

    logTimeSec(timeS, timeFraction, gene)

    if (timeFraction > 0.99) {
      lastAvgTime *= (1 + relief)
      lastAvgTime = Math.min(lastAvgTime, InitialTimeLimit)
    }
    // Finished => [0,0.5], Not finished => [0.5,1]
    0.5 * timeFraction + 0.5 * population.population.minBy(_.fitness).fitness
  }

  def fitness(gene: Params): Double = {
    var sum = 0.0
    for (i <- 0 until Rounds) {
      val result = fitnessOnce(gene)
      sum += result
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
        val intMax: Int = iteratorIndex(geneticParam.intsMax.valuesIterator, i)
        val intMin: Int = iteratorIndex(geneticParam.intsMin.valuesIterator, i)
        val delta = ((rand.nextDouble() * IntsMutationSize - (IntsMutationSize / 2)) * 2) * intMax
        val current = params.ints(i)
        params.ints(i) = (current + delta.toInt) max intMin min intMax
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
      val doubleDist = Distance.arrayDistanceD(x.doubles.iterator, y.doubles.iterator)
      def normInts(arr: Array[Int]): Iterator[Double] =
        arr.iterator.zipWithIndex.map { case (v, i) => v.toDouble / iteratorIndex(geneticParam.intsMax.valuesIterator, i) }
      val intDist = Distance.arrayDistanceD(normInts(x.ints), normInts(y.ints))
      (intDist + doubleDist) / 2
    }
  }

  override def randomElement(rand: Random): Params = GeneticParams.randomParams(geneticParam, rand)

  override def show(gene: Params): String = geneticParam.updateArrayParams(gene).toString

  override def showScientific(): Boolean = false
}

object GeneticParams {
  def randomParams(geneticParam: Parametric[GeneticAlg[_]], rand: Random): Params = new Params(
    Array.tabulate(geneticParam.intNamesDefaults.size)(i => {
      val max: Int = iteratorIndex(geneticParam.intsMax.valuesIterator, i)
      val min: Int = iteratorIndex(geneticParam.intsMin.valuesIterator, i)
      Math.max(rand.nextInt(max), min)
    }),
    Array.fill(geneticParam.doubleNamesDefaults.size)(rand.nextDouble()))

  val emptyParams: Params = new Params(Array.emptyIntArray, Array.emptyDoubleArray)
  val emptyParamsGene: Gene[Params] = new Gene(emptyParams, 0)
}