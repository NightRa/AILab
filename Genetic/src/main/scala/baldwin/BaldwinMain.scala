package baldwin

import genetic.localOptima.IgnoreLocalOptima
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.{RouletteWheelSelection, TopSelection}
import genetic.{GeneticAlg, GeneticMain}
import params.{GeneticParamsMain, Params, NamedParams}
import string.StringHeuristics

import java.util.Random

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
    "Population Size" -> 1000,
    "Max Iterations" -> 1000
  )(
    "Elitism Rate" -> 0,
    "Mutation Rate" -> 0.05,
    "Top Ratio" -> 0.7
  )
  val target: Array[Bit] = targetBits
  val length = target.length
  //def appliedHeuristic(state: Array[Char]) = heuristic(state, target)

  def alg(params: Params, maxTime: Double): GeneticAlg[Array[BaldwinBit]] = {
    val PopulationSize = params.ints(0)
    val maxIterations = params.ints(1)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val TopRatio = params.doubles(2)

    val genetic = new GeneticBaldwin(maxIterations, x => BaldwinBitString.bitStringEquals(x, targetBits), rand)
    val mateStrategy = new ElitismMutationMateStrategy[Array[BaldwinBit]](ElitismRate, MutationRate, rand)
    val selectionStrategy = new RouletteWheelSelection()
    val localOptimaDetector = new IgnoreLocalOptima[Array[BaldwinBit]]()

    new GeneticAlg[Array[BaldwinBit]](genetic,
      mateStrategy,
      selectionStrategy,
      localOptimaDetector,
      PopulationSize,
      MaxTime,
      rand,
      BaldwinBitString.generateBitStringRandomly(length, _),
      BaldwinBitString.show
    )
  }
}

object StartingBits {
  val bits = Array.fill[Bit](10)(Zero)
}

object GeneticBaldwinMain extends BaldwinMain(StartingBits.bits)
object GeneticBaldwinOptimizationMain extends GeneticParamsMain(GeneticBaldwinMain, 40)