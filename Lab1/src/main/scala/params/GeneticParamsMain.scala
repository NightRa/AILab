package params

import java.util.Random

import genetic._
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import genetic.types.Gene
import params.GeneticParamsMain._
import util.JavaUtil

class GeneticParamsMain(main: GeneticMain[_], override val MaxTime: Double, csv: Boolean) extends GeneticMain[Params] {
  override def fullOutput = false
  // Parameters
  val DefaultIntsMutationRatio: Int = 32 // 0
  val DefaultPopulationSize: Int = 20 // 1
  val DefaultRounds: Int = 10 // 2
  val DefaultDoublesMutationSize: Double = 0.1 // 0
  val DefaultMutationRate: Double = 0.5 // 1
  val DefaultElitismRate: Double = 0.3 // 2
  val DefaultTimeLimit: Double = 0.3 // 3
  val DefaultTopRatio: Double = 0.5 // 4
  override val intsMax: Int = 50
  override val defaultParams = Params(DefaultIntsMutationRatio, DefaultPopulationSize, DefaultRounds)(
    DefaultDoublesMutationSize, DefaultMutationRate, DefaultElitismRate, DefaultTimeLimit, DefaultTopRatio)

  // Derived values
  val intsMutationSize: Int = main.intsMax() / DefaultIntsMutationRatio

  def printer(timeSec: Double, timeFraction: Double, params: Params) = {
    val timeMs = JavaUtil.formatDouble(timeSec * 1000, 3)
    if (csv) print(s"$timeMs, ")
    else {
      println(s"$timeMs ms; " + (if (timeFraction > 0.99) params else ""))
    }
  }

  def alg(params: Params, maxTime: Double): GeneticAlg[Params] = {
    val IntsMutationRatio = params.ints(0)
    val PopulationSize = params.ints(1)
    val Rounds = params.ints(2)
    val DoublesMutationSize = params.doubles(0)
    val MutationRate = params.doubles(1)
    val ElitismRate = params.doubles(2)
    val TimeLimit = params.doubles(3)
    val TopRatio = params.doubles(4)

    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)
    val genetic = new GeneticParams(main, IntsMutationRatio, DoublesMutationSize, MutationRate, TimeLimit, Rounds, printer, rand)

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
