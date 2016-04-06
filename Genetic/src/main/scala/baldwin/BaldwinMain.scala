package baldwin

import genetic.localOptima.IgnoreLocalOptima
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.{RouletteWheelSelection, TopSelection}
import genetic.{GeneticAlg, GeneticMain}
import params.{GeneticParamsMain, NamedParams, Params}
import string.StringHeuristics
import java.util.Random

import func.FuncSolution
import genetic.fitnessMapping.FitnessMapping
import genetic.generation.Generation
import genetic.mutation.RegularMutation
import genetic.survivors.Elitism

class BaldwinMain(targetBits: Array[Bit]) extends GeneticMain[Array[BaldwinBit]] {
  val name: String = "Baldwin's bit-string search"
  val MaxTime: Double = 20.0
  val printEvery = 1
  override val intsMax: Int = 1024
  /*override val defaultParams = NamedParams(
    "Population Size" -> 5,
    "Max Iterations" -> 1000
  )(
    "Elitism Rate" -> 0.63,
    "Mutation Rate" -> 0.99,
    "Top Ratio" -> 0.5
  )*/

  // best: 35ms - 75ms (On yuval's computer.. should test it on Ilan's)
  override val defaultParams = NamedParams(
    "Population Size" -> 100,
    "Max Iterations" -> 1000
  )(
    "Elitism Rate" -> 0,
    "Mutation Rate" -> 0.05
  )

  def alg(params: Params): GeneticAlg[Array[BaldwinBit]] = {
    val PopulationSize = params.ints(0)
    val maxIterations = params.ints(1)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)

    val genetic = new GeneticBaldwin(maxIterations, targetBits, rand)

    val selectionStrategy = new RouletteWheelSelection()
    val localOptimaDetector = new IgnoreLocalOptima[Array[BaldwinBit]]()
    val survivorSelection = new Elitism[Array[BaldwinBit]](ElitismRate)
    val mutationStrategy = new RegularMutation(MutationRate, rand)
    val normalGeneration = new Generation[Array[BaldwinBit]](
      selectionStrategy,
      mutationStrategy,
      survivorSelection,
      Array.empty[FitnessMapping],
      rand
    )

    new GeneticAlg[Array[BaldwinBit]](
      genetic,
      localOptimaDetector,
      normalGeneration,
      normalGeneration,
      PopulationSize,
      rand
    )
  }
}

object StartingBits {
  val target = Array.fill[Bit](20)(Zero)
}

object GeneticBaldwinMain extends BaldwinMain(StartingBits.target)