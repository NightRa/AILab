package analysys

import func.GeneticFuncMain
import genetic.GeneticMetadata
import util.{JavaUtil, Util}

class SingleAnalysis[A <: AnyRef](main: GeneticMetadata[A], Rounds: Int) extends  App {
  // val time = Util.avgExecutionTime(main.alg(main.defaultParams(), 1.0).run(print = false), Rounds)
  // println(JavaUtil.formatDouble(time * 1000, 4) + " ms")
}

object SingleFunc extends SingleAnalysis(GeneticFuncMain, 1000)
