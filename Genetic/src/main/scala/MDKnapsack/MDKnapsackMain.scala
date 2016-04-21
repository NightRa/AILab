package mdKnapsack

import java.util.Random

import genetic.{Genetic, GeneticMetadata}
import parametric.Parametric
import util.BitSet

class MDKnapsackMain(instance: MDKnapsackInstance) extends GeneticMetadata[BitSet] {
  override def name: String = "Multi-Dimensional Knapsack"

  override def defaultMaxTime: Double = 1.0
  override def defaultPrintEvery: Int = 1

  override def genetic: Parametric[Genetic[BitSet]] =
    Parametric.point {
      new GeneticMDKnapsack(instance, rand)
    }
}
