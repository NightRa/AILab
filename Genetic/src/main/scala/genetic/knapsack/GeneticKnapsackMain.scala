package genetic.knapsack

import genetic.genetic.{Genetic, GeneticMain, GeneticMetadata}
import genetic.genetic.generation.Crossover
import genetic.genetic.GeneticMain
import genetic.parametric.Parametric
import genetic.parametric.Parametric._
import genetic.params.GeneticParamsMain

class GeneticKnapsackMain(items: Array[Item], userMaxWeight: Double, bestSolution: Option[Double]) extends GeneticMetadata[KnapsackElement] {
  val name = "Knapsack"
  val defaultMaxTime: Double = 1
  val defaultPrintEvery = 1

  val maxWeight = userMaxWeight + 1e-9 // epsilon

  val knapsackInstance = new KnapsackInstance(items.sortBy(_.weight), maxWeight) {
    // May override to optimal for testing.
    override def valueUpperBound: Double = bestSolution match {
      case None => super.valueUpperBound
      case Some(max) => max
    }
  }

  override def genetic: Parametric[Genetic[KnapsackElement]] =
    for {
      mutationProb <- doubleParam("Mutation Probability (For each item when mutating)", 0.5382) // For each value, change (+1) with bernuli(mutationProb)
    } yield new GeneticKnapsack(knapsackInstance, Crossover.onePointCrossoverInt, KnapsackMutate.binomialMutate(mutationProb, _, _), rand)


  // Parameters (9 ms - 20 ms)
  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 7
  )

  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.1685,
    "Mutation Rate" -> 0.8595,
    "Mutation Probability (For each item when mutating)" -> 0.5382
  )
}

object GeneticKnapsackMain extends GeneticKnapsackMain(
  Array(Item(0.22, 0.53), Item(0.33, 0.77), Item(0.1, 0.1), Item(1.2, 3), Item(1.5, 4)),
  10.0001,
  Some(26.36)
) with App {
  GeneticMain.runMain(this)
}

object KnapsackOptimization extends GeneticParamsMain(GeneticKnapsackMain, GeneticKnapsackMain.defaultGeneticAlgParametric, 100) with App {
  GeneticMain.runMain(this)
}

object KnapsackMetaOptimization extends GeneticParamsMain(KnapsackOptimization, KnapsackOptimization.defaultGeneticAlgParametric, 100) with App {
  GeneticMain.runMain(this)
}
