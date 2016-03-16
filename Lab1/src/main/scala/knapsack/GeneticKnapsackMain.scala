package knapsack

import genetic.mating.{Crossover, ElitismMutationMateStrategy}
import genetic.selection.TopSelection
import genetic.{GeneticAlg, GeneticMain}
import params.{GeneticParamsMain, Params}

object GeneticKnapsackMain extends GeneticMain[KnapsackElement] {
  override val MaxTime: Double = 1
  override def fullOutput = true

  // Parameters (9 ms - 20 ms)
  // Ints
  val DefaultPopulationSize: Int = 7 // 0
  // Doubles
  val DefaultElitismRate: Double = 0.1685 // 0
  val DefaultMutationRate: Double = 0.8595 // 1
  // For each value, change (+1) with bernuli(mutationProb)
  val DefaultMutationProb: Double = 0.5382 // 2
  val DefaultTopRatio: Double = 0.7917 // 3
  override val intsMax = 4096
  override val defaultParams = Params(DefaultPopulationSize)(
    DefaultElitismRate, DefaultMutationRate, DefaultMutationProb, DefaultTopRatio)

  val items = Array(Item(0.22, 0.53), Item(0.33, 0.77), Item(0.1, 0.1), Item(1.2, 3), Item(1.5, 4)).sortBy(_.weight)
  val knapsackInstance = new KnapsackInstance(items, 10.0001) {
    // May override to optimal for testing.
    override def valueUpperBound: Double = 26.36
  }

  def alg(params: Params, maxTime: Double): GeneticAlg[KnapsackElement] = {
    val PopulationSize: Int = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val MutationProb = params.doubles(2)
    val TopRatio = params.doubles(3)

    val genetic = new GeneticKnapsack(Crossover.onePointCrossover, KnapsackMutate.binomialMutate(MutationProb, _, _), rand)

    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)

    new GeneticAlg(
      genetic, mateStrategy, selectionStrategy, PopulationSize,
      maxTime, rand,
      KnapsackElement.randomKnapsack(knapsackInstance, _),
      _.toString)
  }
}

object KnapsackOptimization extends GeneticParamsMain(GeneticKnapsackMain, 100, csv = false)
