package genetic.func

import genetic.genetic.GeneticMetadata
import genetic.genetic.Genetic
import genetic.parametric.Parametric
import genetic.parametric.Parametric._

class GeneticFuncMain(func: Func) extends GeneticMetadata[FuncSolution] {
  val name = func.name + " Optimization"

  def defaultPrintEvery = 1

  def defaultMaxTime: Double = 1

  override def genetic: Parametric[Genetic[FuncSolution]] =
    for {
      mutationSize <- doubleParam("Mutation Size", 0.035)
    } yield new GeneticFunc(func, mutationSize, rand)

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 50
  )

  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.3,
    "Mutation Rate" -> 0.4,
    "Mutation Size" -> 0.2,
    "Top Ratio" -> 0.75,
    "Gene Similarity Threshold" -> 0.05
  )
}

object GeneticFuncMain extends GeneticFuncMain(HoldersTableFunction)

