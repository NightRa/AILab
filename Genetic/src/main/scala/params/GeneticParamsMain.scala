package params

import genetic._
import parametric.Parametric
import parametric.Parametric._
import util.JavaUtil

class GeneticParamsMain(geneticMeta: GeneticMetadata[_], geneticAlgParams: Parametric[GeneticAlg[_]], val defaultMaxTime: Double = 100.0) extends GeneticMetadata[Params] {
  override val name = geneticMeta.name + " *Optimization*"
  override def defaultPrintEvery = 1
  override def isOpt: Boolean = true

  override def genetic: Parametric[Genetic[Params]] =
    for {
      rounds              <- intParam   ("Rounds (for stability)", default = 10, minValue = 1, maxValue = 100)
      intsMutationSize    <- doubleParam("Ints Mutation Size"              , 0.1)
      doublesMutationSize <- doubleParam("Doubles Mutation Size "          , 0.1)
      mutationRate        <- doubleParam("Mutation Rate"                   , 0.5)
      timeLimit           <- doubleParam("Time Limit per config (Seconds)" , 0.3)
      pressure            <- doubleParam("Pressure: Dynamic window of time from min. time, [0.1,1] multiplied by 10", 0.4)
      relief              <- doubleParam("Relief: Percent of relief when bumping the time limit", 0.05)
    } yield new GeneticParams(geneticAlgParams, intsMutationSize, doublesMutationSize, mutationRate, timeLimit, rounds, pressure, relief, printer, rand)

  def printer(timeSec: Double, timeFraction: Double, params: Params) = {
    val timeMs = JavaUtil.formatDouble(timeSec * 1000, 3)
    s"$timeMs ms; " + (if (timeFraction > 0.99) params else "")
  }

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 30
  )
}

