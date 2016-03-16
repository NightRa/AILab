package func

import genetic._
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import params.{GeneticParamsMain, Params}

object GeneticFuncMain extends GeneticMain[Func] {
  override val MaxTime: Double = 5.0
  override def fullOutput = true

  // Params
  // Ints
  val DefaultPopulationSize: Int = 190 // 0
  // Doubles
  val DefaultElitismRate: Double = 0.441 // 0
  val DefaultMutationRate: Double = 0.46 // 1
  val DefaultMutationSize: Double = 0.254 // 2
  val DefaultTopRatio: Double = 0.5 // 3
  override val intsMax = 1024 * 16
  override val defaultParams = Params(DefaultPopulationSize)(
    DefaultElitismRate, DefaultMutationRate, DefaultMutationSize, DefaultTopRatio)

  def alg(params: Params, maxTime: Double): GeneticAlg[Func] = {
    val PopulationSize = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val MutationSize = params.doubles(2)
    val TopRatio = params.doubles(3)

    val genetic = new GeneticFunc(MutationSize, rand)
    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)

    new GeneticAlg[Func](
      genetic, mateStrategy, selectionStrategy, PopulationSize,
      maxTime, rand,
      Func.genFunc,
      _.toString)
  }
}

object GeneticFuncOptimization extends GeneticParamsMain(GeneticFuncMain, 100, csv = false)
