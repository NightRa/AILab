package queens

import java.util.Random

import func.FuncSolution
import genetic.fitnessMapping.FitnessMapping
import genetic.generation.Generation
import genetic.localOptima.IgnoreLocalOptima
import genetic.mating.ElitismMutationMateStrategy
import genetic.mutation.RegularMutation
import genetic.selection.TopSelection
import genetic.survivors.Elitism
import genetic.{GeneticAlg, GeneticMain}
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

  def alg(params: Params): GeneticAlg[QueenPermutation] = {
    val PopulationSize = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val TopRatio = params.doubles(2)

    val genetic = new GeneticQueen(boardSize, queenMating, queenMutation, rand)

    val selectionStrategy = new TopSelection(TopRatio)
    val localOptimaDetector = new IgnoreLocalOptima[QueenPermutation]()
    val survivorSelection = new Elitism[QueenPermutation](ElitismRate)
    val mutationStrategy = new RegularMutation(MutationRate, rand)
    val normalGeneration = new Generation[QueenPermutation](
      selectionStrategy,
      mutationStrategy,
      survivorSelection,
      Array.empty[FitnessMapping],
      rand
    )

    new GeneticAlg[QueenPermutation](
      genetic,
      localOptimaDetector,
      normalGeneration,
      normalGeneration,
      PopulationSize,
      rand
    )
  }
}

object GeneticQueenMain extends GeneticQueenMain(10, QueenMating.pmx, QueenMutation.complexInversion) {
  def showBoard(permutation: Array[Int]): String = {
    val size = permutation.length
    permutation.map(i => "- " * (i - 1) + "x " + "- " * (size - i)).mkString("\n", "\n", "\n")
  }
}
