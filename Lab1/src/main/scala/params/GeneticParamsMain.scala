package params

import java.util.Random

import genetic._
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import genetic.types.Gene
import params.GeneticParamsMain._
import util.JavaUtil

class GeneticParamsMain(main: GeneticMain[_], override val MaxTime: Double) extends GeneticMain[Params] {
  override def fullOutput = false
  val name = "Genetic Parameter Search"
  override val intsMax: Int = 48
  override val defaultParams = NamedParams(
    "Ints Mutation Size" -> 32,
    "Population Size" -> 20,
    "Rounds" -> 10
  )(
    "Doubles Mutation Size" -> 0.1,
    "Mutation Rate" -> 0.5,
    "Elitism Rate" -> 0.16,
    "Time Limit (Seconds)" -> 0.3,
    "Top Ratio" -> 0.8
  )

  def printer(timeSec: Double, timeFraction: Double, params: Params) = {
    val timeMs = JavaUtil.formatDouble(timeSec * 1000, 3)
    s"$timeMs ms; " + (if (timeFraction > 0.99) params else "")
  }

  def alg(params: Params, maxTime: Double): GeneticAlg[Params] = {
    val IntsMutationSize = params.ints(0)
    val PopulationSize = params.ints(1)
    val Rounds = params.ints(2)
    val DoublesMutationSize = params.doubles(0)
    val MutationRate = params.doubles(1)
    val ElitismRate = params.doubles(2)
    val TimeLimit = params.doubles(3)
    val TopRatio = params.doubles(4)

    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)
    val genetic = new GeneticParams(main, IntsMutationSize, DoublesMutationSize, MutationRate, TimeLimit, Rounds, printer, rand)

    new GeneticAlg[Params](
      genetic, mateStrategy, selectionStrategy, PopulationSize,
      maxTime, rand,
      randomParams(main, _),
      _.toString)
  }
}

object GeneticParamsMain {
  def randomParams(main: GeneticMain[_], rand: Random): Params = new Params(
    Array.fill(main.intsSize)(rand.nextInt(main.intsMax())),
    Array.fill(main.doublesSize())(rand.nextDouble()))

  val emptyParams: Params = new Params(Array.emptyIntArray, Array.emptyDoubleArray)
  val emptyParamsGene: Gene[Params] = new Gene(emptyParams, 0)
}
