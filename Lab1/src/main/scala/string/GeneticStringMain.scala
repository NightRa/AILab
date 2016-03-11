package string

import java.util.Random

import genetic._
import params.{GeneticParamsMain, Params}
import util.Util

object GeneticStringMain extends GeneticMain[Array[Char]] {
  val MaxTime: Double = 5.0
  // Parameters
  val PopulationSize: Int = 3
  val ElitismRate: Double = 0.447
  val MutationRate: Double = 0.939

  val TargetString: Array[Char] = "Hello world! How are you doing today? My name is Ilan. Hello world! How are you doing today? My name is Ilan.".toCharArray


  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  val genetic = new GeneticString(StringHeuristics.heuristic2(_, TargetString), rand)

  def mateStrategy(params: Params) = {
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    new OnePointCrossover(ElitismRate, MutationRate, rand)
  }

  def initPopulation(params: Params) = new Population[Array[Char]](Array.fill(params.ints(0))(new Gene(Util.randString(TargetString.length, rand), 0)))

  def initBuffer(params: Params) = new Population[Array[Char]](Array.fill(params.ints(0))(new Gene(Array.emptyCharArray, 0)))


  def intsSize(): Int = 1

  def intsMax(): Int = 128

  def doublesSize(): Int = 2

  def alg(params: Params, maxTime: Double): GeneticAlg[Array[Char]] =
    new GeneticAlg(genetic, mateStrategy(params), maxTime, rand, initPopulation(params), initBuffer(params), (arr: Array[Char]) => arr.mkString)

  def main(args: Array[String]) {
    val start = System.currentTimeMillis()
    val (pop, iterations) = alg(new Params(Array(PopulationSize), intsMax(), Array(ElitismRate, MutationRate)), MaxTime).run(print = false)
    val end = System.currentTimeMillis()
    val time = end - start
    println(s"$time ms, $iterations iterations")
  }

}

object GeneticStringOptimization extends GeneticParamsMain(GeneticStringMain)
