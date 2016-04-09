package func

import genetic._
import parametric.Parametric
import parametric.Parametric._

class GeneticFuncMain(func: Func) extends GeneticMetadata[FuncSolution] {
  val name = func.name + " Optimization"

  def defaultPrintEvery = 1

  def defaultMaxTime: Double = 1

  override def genetic: Parametric[Genetic[FuncSolution]] =
    for {
      mutationSize <- doubleParam("Mutation Size", 0.035)
    } yield new GeneticFunc(func, mutationSize, rand)

  // 6.07 ms: 190, 0.441, 0.46, 0.254, 0.5
  // 2.76 ms: 180, 0.16, 0.31, 0.035, 0.8

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 180
  )

  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.16,
    "Mutation Rate" -> 0.31,
    "Mutation Size" -> 0.035
  )
}

object GeneticFuncMain extends GeneticFuncMain(HoldersTableFunction)

