package mdKnapsack

import genetic.{Genetic, GeneticMetadata}
import parametric.Parametric
import util.BitSet

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
    "Population Size" -> 160
  )

  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.825,
    "Gene Similarity Threshold" -> 0.434,
    "Local Optimum: Elitism Rate" -> 0.105,
    "Local Optimum: Hyper Mutation Rate" -> 0.417,
    "Local Optimum: Immigrants Rate" -> 0.362,
    "Local Optimum: Top Ratio" -> 0.99,
    "Mutation Rate" -> 0.512,
    "Top Ratio" -> 0.2
  )
}
