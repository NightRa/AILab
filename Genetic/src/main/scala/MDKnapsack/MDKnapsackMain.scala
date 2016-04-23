package mdKnapsack

import genetic.{Genetic, GeneticEngine, GeneticMetadata}
import parametric.{Instances, Parametric}
import util.BitSet
import Instances._
import genetic.generation.Generation
import genetic.survivors.construction.{DeduplicatedConstruction, NormalConstruction}

class MDKnapsackMain(instance: MDKnapsackInstance) extends GeneticMetadata[BitSet] {
  override def name: String = "Multi-Dimensional Knapsack"

  override def defaultMaxTime: Double = 2.0
  override def defaultPrintEvery: Int = 1000

  override def genetic: Parametric[Genetic[BitSet]] =
    Parametric.point {
      new GeneticMDKnapsack(instance, rand)
    }

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 216
  )

  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.34,
    "Gene Similarity Threshold" -> 0.025,
    "Local Optimum: Elitism Rate" -> 0.525,
    "Local Optimum: Hyper Mutation Rate" -> 0.52,
    "Local Optimum: Immigrants Rate" -> 0.074,
    "Local Optimum: Top Ratio" -> 0.064,
    "Mutation Rate" -> 0.624,
    "Top Ratio" -> 0.988
  )

  // To be overwritten with problem-specific defaults.
  override def defaultEngine: Parametric[GeneticEngine] = {
    val normalGeneration = for {
      selectionStrategy <- topSelection
      mutationStrategy <- mutation
      elitism <- elitism
    } yield new Generation(selectionStrategy, mutationStrategy, Array(elitism), new DeduplicatedConstruction, Array())

    val localOptimaGeneration = for {
      selectionStrategy <- topSelection
      mutationStrategy <- hyperMutation
      elitism <- elitism
      immigrants <- randomImmigrantsElitism
    } yield new Generation(selectionStrategy, mutationStrategy, Array(elitism, immigrants), new DeduplicatedConstruction, Array())

    geneticEngine(geneSimilarity, normalGeneration, localOptimaGeneration)
  }

}
