package params

import genetic._
import parametric.Parametric
import parametric.Parametric._
import util.JavaUtil

class GeneticParamsMain(geneticAlgParams: Parametric[GeneticAlg[_]], val defaultMaxTime: Double = 100.0) extends GeneticMetadata[Params] {
  override def defaultPrintEvery = 1

  override val name = "Genetic Parameter Search"

  override def genetic: Parametric[Genetic[Params]] =
    for {
      rounds              <- intParam   ("Rounds", default = 10, minValue = 1, maxValue = 100)
      intsMutationSize    <- doubleParam("Ints Mutation Size"   , 0.1)
      doublesMutationSize <- doubleParam("Doubles Mutation Size", 0.1)
      mutationRate        <- doubleParam("Mutation Rate"        , 0.5)
      timeLimit           <- doubleParam("Time Limit (Seconds)" , 0.3)
    } yield new GeneticParams(geneticAlgParams, intsMutationSize, doublesMutationSize, mutationRate, timeLimit, rounds, printer, rand)

  def printer(timeSec: Double, timeFraction: Double, params: Params) = {
    val timeMs = JavaUtil.formatDouble(timeSec * 1000, 3)
    s"$timeMs ms; " + (if (timeFraction > 0.99) params else "")
  }
}

