package func

import genetic._
import genetic.localOptima.IgnoreLocalOptima
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import params.{GeneticParamsMain, NamedParams, Params}

class GeneticFuncMain(func: Func) extends GeneticMain[FuncSolution] {
  val name = func.name + " Optimization"
  override val MaxTime: Double = 5.0
  override def printEvery = 1

  // 6.07 ms: 190, 0.441, 0.46, 0.254, 0.5
  // 2.76 ms: 180, 0.16, 0.31, 0.035, 0.8

  override val defaultParams = NamedParams(
    "Population Size" -> 180
  )(
    "Elitism Rate" -> 0.16,
    "Mutation Rate" -> 0.31,
    "Mutation Size" -> 0.035,
    "Top Ratio" -> 0.8
  )
  override val intsMax = 1024

  def alg(params: Params, maxTime: Double): GeneticAlg[FuncSolution] = {
    val PopulationSize = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val MutationSize = params.doubles(2)
    val TopRatio = params.doubles(3)

    val genetic = new GeneticFunc(func, MutationSize, rand)
    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)
    val localOptimaDetector = new IgnoreLocalOptima()

    new GeneticAlg[FuncSolution](
      genetic, mateStrategy, selectionStrategy, localOptimaDetector,
      PopulationSize,
      maxTime, rand,
      FuncSolution.genFuncSolution(func, _),
      _.toString)
  }
}

object GeneticFuncMain extends GeneticFuncMain(HoldersTableFunction)

object GeneticFuncOptimization extends GeneticParamsMain(GeneticFuncMain, 100)
