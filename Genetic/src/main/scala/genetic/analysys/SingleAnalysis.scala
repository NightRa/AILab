package genetic.analysys

import genetic.genetic.GeneticMetadata
import genetic.knapsack.GeneticKnapsackMain
import genetic.util.{JavaUtil, Util}

class SingleAnalysis[A <: AnyRef](metaGenetic: GeneticMetadata[A], Rounds: Int) extends App {
  val time = Util.avgExecutionTime(metaGenetic.defaultGeneticAlgParametric.applyDefaults().run(printEvery = 0, 0.3), Rounds)
  println(JavaUtil.formatDouble(time * 1000, 4) + " ms")
}

object SingleFunc extends SingleAnalysis(GeneticKnapsackMain, 1000)
