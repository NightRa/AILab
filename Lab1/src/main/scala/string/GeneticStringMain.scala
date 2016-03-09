package string

import java.util.Random

import genetic.{Gene, GeneticAlg, OnePointCrossover, Population}
import util.Util

object GeneticStringMain extends App {
  val PopulationSize: Int = 2048
  val MaxTime: Double = 5.0
  val ElitismRate: Float = 0.10f
  val MutationRate: Float = 0.99f
  val TargetString: Array[Char] = "Hello world! How are you doing today? My name is Ilan. Hello world! How are you doing today? My name is Ilan.".toCharArray

  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  val alg = new GeneticString(StringHeuristics.heuristic1(_, TargetString), rand)
  val mateStrategy = new OnePointCrossover(ElitismRate, MutationRate, rand)
  val geneticAlg = new GeneticAlg(alg, mateStrategy, MaxTime, rand, (arr: Array[Char]) => arr.mkString)

  val population = new Population[Array[Char]](Array.fill(PopulationSize)(new Gene(Util.randString(TargetString.length, rand), 0)))
  val buffer = new Population[Array[Char]](Array.fill(PopulationSize)(new Gene(Array.emptyCharArray, 0)))

  geneticAlg.run(population, buffer, print = true)
}
