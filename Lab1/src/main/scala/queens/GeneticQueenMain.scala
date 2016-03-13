package queens

import genetic.mating.OnePointCrossover
import genetic.selection.TopSelection
import genetic.types.{Gene, Population}
import genetic.{Genetic, GeneticAlg, GeneticMain}
import params.Params

import java.util.Random

object GeneticQueenMain extends GeneticMain[QueenPermutation] {
  val MaxTime: Double = 5.0

  val Size: Int = 10

  // 8 ms - 10 ms
  // Params
  // Ints
  val PopulationSize: Int = 100
  // 0
  // Doubles
  val ElitismRate: Double = 0.1
  // 0
  val MutationRate: Double = 0.4
  // 1
  val MutationSize: Double = 0.8
  // 2
  val TopRatio: Double = 0.62 // 3

  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  def genetic(params: Params): Genetic[QueenPermutation] = new GeneticQueen(params.doubles(2),
    rand,
    (as, bs, rnd) => QueenMating.cx(as, bs, rnd),
    (as, rnd) => QueenMutation.complexInversion(as, rnd))

  def mateStrategy(params: Params) = new OnePointCrossover(params.doubles(0), params.doubles(1), rand)

  def selectionStrategy(params: Params) = new TopSelection(params.doubles(3))

  def initPopulation(params: Params) = new Population[QueenPermutation](Array.fill(params.ints(0))(
    new Gene(QueenPermutation.getPermutationRandomly(Size, rand), 0)))

  def initBuffer(params: Params) = new Population[QueenPermutation](Array.fill(params.ints(0))(
    new Gene(new QueenPermutation(Array.emptyIntArray), 0)))

  def intsSize(): Int = 1

  def intsMax(): Int = 1024 * 16

  def doublesSize(): Int = 4

  def alg(params: Params, maxTime: Double): GeneticAlg[QueenPermutation] =
    new GeneticAlg[QueenPermutation](
      genetic(params), mateStrategy(params), selectionStrategy(params), maxTime,
      rand, initPopulation(params), initBuffer(params), _.permutation.mkString("[", ",", "]"))

  def showBoard(permutation: Array[Int]): String = {
    val size = permutation.length
    permutation.map(i => "- " * (i - 1) + "x " + "- " * (size - i)).mkString("\n", "\n", "\n")
  }

  def main(args: Array[String]) {
    val start = System.currentTimeMillis()
    alg(new Params(Array(PopulationSize), intsMax(), Array(ElitismRate, MutationRate, MutationSize, TopRatio)), MaxTime).run(print = true)
    val end = System.currentTimeMillis()
    val time = end - start

    println(s"$time ms")
  }
}
