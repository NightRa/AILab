package baldwin

import genetic.{Genetic, GeneticMetadata}
import parametric.Parametric
import parametric.Parametric._


class BaldwinMain(targetBits: Array[Bit]) extends GeneticMetadata[Array[BaldwinBit]] {
  val name: String = "Baldwin's bit-string search"
  val defaultMaxTime: Double = 20.0
  val defaultPrintEvery = 1

  override def genetic: Parametric[Genetic[Array[BaldwinBit]]] =
    for {
      maxIterations <- intParam("Max Iterations", default = 1000, minValue = 1, maxValue = 2000)
    } yield new GeneticBaldwin(maxIterations, targetBits, rand)

}

object StartingBits {
  val target = Array.fill[Bit](20)(Zero)
}

object GeneticBaldwinMain extends BaldwinMain(StartingBits.target)