package knapsack

import func.FuncSolution
import genetic.fitnessMapping.FitnessMapping
import genetic.localOptima.IgnoreLocalOptima
import genetic.generation.{Crossover, Generation}
import genetic.mutation.RegularMutation
import genetic.selection.TopSelection
import genetic.survivors.Elitism
import genetic.{GeneticAlg, GeneticMain}
import params.{GeneticParamsMain, NamedParams, Params}

class GeneticKnapsackMain(items: Array[Item], userMaxWeight: Double, bestSolution: Option[Double]) extends GeneticMain[KnapsackElement] {
  val name = "Knapsack"
  val MaxTime: Double = 1
  val printEvery = 1

  val maxWeight = userMaxWeight + 1e-9 // epsilon

  // Parameters (9 ms - 20 ms)
  override val intsMax = 4096
  override val defaultParams = NamedParams(
    "Population Size" -> 7
  )(
    "Elitism Rate" -> 0.1685,
    "Mutation Rate" -> 0.8595,
    "Mutation Probability (For each item when mutating)" -> 0.5382, // For each value, change (+1) with bernuli(mutationProb)
    "Top Ratio" -> 0.7917
  )

  val knapsackInstance = new KnapsackInstance(items.sortBy(_.weight), maxWeight) {
    // May override to optimal for testing.
    override def valueUpperBound: Double = bestSolution match {
      case None => super.valueUpperBound
      case Some(max) => max
    }
  }

  def alg(params: Params): GeneticAlg[KnapsackElement] = {
    val PopulationSize: Int = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val MutationProb = params.doubles(2)
    val TopRatio = params.doubles(3)

    val genetic = new GeneticKnapsack(knapsackInstance, Crossover.onePointCrossoverInt, KnapsackMutate.binomialMutate(MutationProb, _, _), rand)

    val selectionStrategy = new TopSelection(TopRatio)
    val localOptimaDetector = new IgnoreLocalOptima[KnapsackElement]()
    val survivorSelection = new Elitism[KnapsackElement](ElitismRate)
    val mutationStrategy = new RegularMutation(MutationRate, rand)
    val normalGeneration = new Generation[KnapsackElement](
      selectionStrategy,
      mutationStrategy,
      survivorSelection,
      Array.empty[FitnessMapping],
      rand
    )

    new GeneticAlg[KnapsackElement](
      genetic,
      localOptimaDetector,
      normalGeneration,
      normalGeneration,
      PopulationSize,
      rand
    )
  }
}

object GeneticKnapsackMain extends GeneticKnapsackMain(
  Array(Item(0.22, 0.53), Item(0.33, 0.77), Item(0.1, 0.1), Item(1.2, 3), Item(1.5, 4)),
  10.0001,
  Some(26.36)
)

object KnapsackOptimization extends GeneticParamsMain(GeneticKnapsackMain, 100)
