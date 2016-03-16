package string

import java.util.Random

import genetic._
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import genetic.types.{Gene, Population}
import params.{GeneticParamsMain, Params}
import util.Util

object GeneticStringMain extends GeneticMain[Array[Char]] {
  val MaxTime: Double = 5.0
  // Parameters
  // Ints
  val PopulationSize: Int = 3 // 0
  // Doubles
  val ElitismRate: Double = 0.447 // 0
  val MutationRate: Double = 0.939 // 1
  val TopRatio: Double = 0.5 // 2

  val TargetString: Array[Char] = "Hello world! How are you doing today? My name is Ilan. Hello world! How are you doing today? My name is Ilan.".toCharArray


  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  val genetic = new GeneticString(StringHeuristics.heuristic2(_, TargetString), rand)

  def mateStrategy(params: Params) = {
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
  }

  def selectionStrategy(params: Params) = new TopSelection(params.doubles(2))

  def initPopulation(params: Params) = new Population[Array[Char]](Array.fill(params.ints(0))(new Gene(Util.randString(TargetString.length, rand), 0)))

  def initBuffer(params: Params) = new Population[Array[Char]](Array.fill(params.ints(0))(new Gene(Array.emptyCharArray, 0)))


  def intsSize(): Int = 1

  def intsMax(): Int = 128

  def doublesSize(): Int = 3

  def alg(params: Params, maxTime: Double): GeneticAlg[Array[Char]] =
    new GeneticAlg(
      genetic, mateStrategy(params), selectionStrategy(params), maxTime,
      rand, initPopulation(params), initBuffer(params), (arr: Array[Char]) => arr.mkString)

  def main(args: Array[String]) {
    val start = System.currentTimeMillis()
    val (pop, iterations) = alg(new Params(Array(PopulationSize), Array(ElitismRate, MutationRate, TopRatio)), MaxTime).run(print = false)
    val end = System.currentTimeMillis()
    val time = end - start
    println(s"$time ms, $iterations iterations")
  }

}

object GeneticStringOptimization extends GeneticParamsMain(GeneticStringMain, 100, csv = false)
