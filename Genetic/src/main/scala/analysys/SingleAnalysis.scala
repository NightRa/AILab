package analysys

import func.GeneticFuncMain
import genetic.GeneticMetadata
import knapsack.GeneticKnapsackMain
import string.GeneticStringMain
import util.{JavaUtil, Util}

class SingleAnalysis[A <: AnyRef](metaGenetic: GeneticMetadata[A], Rounds: Int) extends  App {
  val time = Util.avgExecutionTime(metaGenetic.defaultGeneticAlgParametric.applyDefaults().run(printEvery = 0, 0.3), Rounds)
  println(JavaUtil.formatDouble(time * 1000, 4) + " ms")
}

object SingleFunc extends SingleAnalysis(GeneticKnapsackMain, 1000)
