package queens

import java.util.{Optional, Random}

import genetic.LocalOptimaEscapingMateStrategy.{Niching, HyperMutation}
import genetic.localOptima.{StdDevLocalOptimaDetector, IgnoreLocalOptima}
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import genetic.{LocalOptimaEscapingMateStrategy, GeneticAlg, GeneticMain}
import params.{NamedParams, Params}

class GeneticQueenMain(boardSize: Int,
                       queenMating: (Array[Int], Array[Int], Random) => Array[Int],
                       queenMutation: (Array[Int], Random) => Unit)
  extends GeneticMain[QueenPermutation] {

  val name = "N-Queens"
  override val MaxTime: Double = 5.0
  override def printEvery = 1

  // 8 ms - 10 ms
  override val intsMax = 1024
  override val defaultParams = NamedParams(
    "Population Size" -> 100
  )(
    "Elitism Rate" -> 0.1,
    "Mutation Rate" -> 0.4,
    "Top Ratio" -> 0.62
  )

  def alg(params: Params, maxTime: Double): GeneticAlg[QueenPermutation] = {
    val PopulationSize = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val TopRatio = params.doubles(2)

    val genetic = new GeneticQueen(queenMating, queenMutation, rand)

    //val mateStrategy = new ElitismMutationMateStrategy[QueenPermutation](ElitismRate, MutationRate, rand)
    val hyperMutation = new HyperMutation(0.95)
    val nieching = new Niching[QueenPermutation](1.0, 0.5, genetic.metric())
    val mateStrategy = new LocalOptimaEscapingMateStrategy[QueenPermutation]( ElitismRate,
                                                                              MutationRate,
                                                                              rand,
                                                                              Optional.empty(),
                                                                              Optional.of(nieching),
                                                                              Optional.of(hyperMutation))
    val selectionStrategy = new TopSelection(TopRatio)
    val localOptimaDetector = new StdDevLocalOptimaDetector[QueenPermutation](0.007)

    new GeneticAlg[QueenPermutation](
      genetic, mateStrategy, selectionStrategy,localOptimaDetector,
      PopulationSize,
      maxTime, rand,
      QueenPermutation.getPermutationRandomly(boardSize, _),
      _.permutation.mkString("[", ",", "]"))
  }
}

object GeneticQueenMain extends GeneticQueenMain(10, QueenMating.pmx, QueenMutation.complexInversion) {
  def showBoard(permutation: Array[Int]): String = {
    val size = permutation.length
    permutation.map(i => "- " * (i - 1) + "x " + "- " * (size - i)).mkString("\n", "\n", "\n")
  }
}
