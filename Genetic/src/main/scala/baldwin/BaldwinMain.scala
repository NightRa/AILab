package baldwin

import genetic.{Genetic, GeneticMain, GeneticMetadata}
import parametric.Parametric
import parametric.Parametric._


class BaldwinMain(targetBits: Array[Byte]) extends GeneticMetadata[Array[Byte]] {
  val name: String = "Baldwin's bit-string search"
  val defaultMaxTime: Double = 2.0
  val defaultPrintEvery = 1

  override def genetic: Parametric[Genetic[Array[Byte]]] =
    for {
      maxIterations <- intParam("Max Iterations", default = 1000, minValue = 1, maxValue = 2000)
    } yield new GeneticBaldwin(maxIterations, targetBits, rand)

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 1000
  )
  override def intsNamesMax: Map[String, Int] = Map(
    "Population Size" -> 1000
  )
  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.14,
    "Mutation Rate" -> 0.7
  )
  //(Max Iterations -> 636, Population Size -> 159), doubles: (Elitism Rate -> 0.146, Mutation Rate -> 0.706))
}

object StartingBits {
  val target = Array.fill[Byte](20)(0)
}

object GeneticBaldwinMain extends BaldwinMain(StartingBits.target) with App {
  GeneticMain.runMain(this)
}