package queens

import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import genetic.{GeneticAlg, GeneticMain}
import params.Params

object GeneticQueenMain extends GeneticMain[QueenPermutation] {
  override val MaxTime: Double = 5.0
  override def fullOutput = true

  // 8 ms - 10 ms
  // Params
  // Ints
  val DefaultPopulationSize: Int = 100 // 0
  // Doubles
  val DefaultElitismRate: Double = 0.1 // 0
  val DefaultMutationRate: Double = 0.4 // 1
  val DefaultTopRatio: Double = 0.62 // 2
  override val intsMax = 1024 * 16
  override val defaultParams = Params(DefaultPopulationSize)(
    DefaultElitismRate, DefaultMutationRate, DefaultTopRatio)

  val BoardSize: Int = 10

  def showBoard(permutation: Array[Int]): String = {
    val size = permutation.length
    permutation.map(i => "- " * (i - 1) + "x " + "- " * (size - i)).mkString("\n", "\n", "\n")
  }

  def alg(params: Params, maxTime: Double): GeneticAlg[QueenPermutation] = {
    val PopulationSize = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val TopRatio = params.doubles(2)

    val genetic = new GeneticQueen(
      rand,
      (as, bs, rnd) => QueenMating.cx(as, bs, rnd),
      (as, rnd) => QueenMutation.complexInversion(as, rnd))

    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)

    new GeneticAlg[QueenPermutation](
      genetic, mateStrategy, selectionStrategy, PopulationSize,
      maxTime, rand,
      QueenPermutation.getPermutationRandomly(BoardSize, _),
      _.permutation.mkString("[", ",", "]"))
  }
}

