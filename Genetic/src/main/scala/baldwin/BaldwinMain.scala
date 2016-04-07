package baldwin

import genetic.localOptima.IgnoreLocalOptima
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.{RouletteWheelSelection, TopSelection}
import genetic.{Genetic, GeneticAlg, GeneticMetadata}
import params.{GeneticParamsMain, NamedParams, Params}
import string.StringHeuristics
import java.util.Random

import func.FuncSolution
import genetic.fitnessMapping.FitnessMapping
import genetic.generation.Generation
import genetic.mutation.RegularMutation
import genetic.survivors.Elitism
import parametric.Parametric
import parametric.Parametric._


class BaldwinMain(targetBits: Array[Bit]) extends GeneticMetadata[Array[BaldwinBit]] {
  val name: String = "Baldwin's bit-string search"
  val defaultMaxTime: Double = 20.0
  val defaultPrintEvery = 1

  override def genetic: Parametric[Genetic[Array[BaldwinBit]]] =
    for {
      maxIterations <- intParam("Max Iterations", default = 1000, maxValue = 2000)
    } yield new GeneticBaldwin(maxIterations, targetBits, rand)

}

object StartingBits {
  val target = Array.fill[Bit](20)(Zero)
}

object GeneticBaldwinMain extends BaldwinMain(StartingBits.target)