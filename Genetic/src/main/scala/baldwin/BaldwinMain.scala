package baldwin

import genetic.localOptima.IgnoreLocalOptima
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import genetic.{GeneticAlg, GeneticMain}
import params.{GeneticParamsMain, Params, NamedParams}
import string.StringHeuristics

import scala.util.Random

class BaldwinMain(targetBits: Array[Bit],
                        mate: (Array[Bit], Array[Bit], Random) => Array[Bit],
                        mutate: (Array[Bit], Random) => Unit) extends GeneticMain[Array[Bit]] {
  val name: String = "Baldwin's bit-string search"
  val MaxTime: Double = 5.0
  val printEvery = 1000000
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
    "Population Size" -> 3,
    "Max Iterations" -> 405
  )(
    "Elitism Rate" -> 0.522,
    "Mutation Rate" -> 0.387,
    "Top Ratio" -> 0.725
  )
  val target: Array[Bit] = targetBits

  //def appliedHeuristic(state: Array[Char]) = heuristic(state, target)

  def alg(params: Params, maxTime: Double): GeneticAlg[Array[Bit]] = {
    val PopulationSize = params.ints(0)
    val maxIterations = params.ints(1)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val TopRatio = params.doubles(2)

    val genetic = new GeneticBaldwin(mate,mutate, rand, maxIterations, targetBits)
    val mateStrategy = new ElitismMutationMateStrategy[Array[Bit]](ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)
    val localOptimaDetector = new IgnoreLocalOptima[Array[Bit]]()

    new GeneticAlg[Array[Bit]](genetic,
      mateStrategy,
      selectionStrategy,
      localOptimaDetector,
      PopulationSize,
      MaxTime,
      rand,
      rnd => BaldwinBitString.genRandomly(targetBits, maxIterations, a => BaldwinUtils.dist(a, targetBits), rnd),
      BaldwinBitString.show
    )
  }
}

object StartingBits {
  def genBits () : Array[Bit] = {
    val length = 40
    val array = new Array[Bit](length)
    for (i <- 0 until length){
      array(i) = Zero()
    }
    array
  }

  val bits = genBits ()
}

object GeneticBaldwinMain extends BaldwinMain(StartingBits.bits, BaldwinBitString.onePointCrossOver, BaldwinBitString.mutateRandomly)
object GeneticBaldwinOptimizationMain extends GeneticParamsMain(GeneticBaldwinMain, 40)