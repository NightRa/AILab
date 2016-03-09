package string

import java.util.Random

import genetic._
import params.{GeneticParamsMain, Params}
import util.Util

object GeneticStringMain extends GeneticMain[Array[Char]] {
  val MaxTime: Double = 5.0

  // For heuristic 2:
  // 65 ms - 90 ms
  // For heuristic 3:
  // 160 ms - 200 ms
  /*val PopulationSize: Int = 2048
  val ElitismRate: Double = 0.10
  val MutationRate: Double = 0.99*/

  // Params: [41] [0.4312166551202713,0.7195631214946957]; fitness: 0.003; avg: 0.005; stdDev: 0.004
  // For Heuristic 2
  // One Shot: 13 ms - 30 ms
  // In optimization: ~8 ms
  /*val PopulationSize: Int = 41
  val ElitismRate: Double = 0.431
  val MutationRate: Double = 0.719*/

  // Params: [2] [0.6096818834473224,0.8954867634899712]; fitness: 0.0025; avg: 0.008; stdDev: 0.005
  // For heuristic 2
  // One shot: 18 ms - 26 ms
  // In optimization: ~5 ms
  /*val PopulationSize: Int = 2
  val ElitismRate: Double = 0.609
  val MutationRate: Double = 0.895*/

  // Params: [232] [0.16556571135326148,0.6008747436657755]; fitness: 0.0125; avg: 0.021; stdDev: 0.007
  // For Heuristic 3
  // One shot: 39 ms - 60 ms
  // In optimization: 30-40 ms
  /*val PopulationSize: Int = 232
  val ElitismRate: Double = 0.165
  val MutationRate: Double = 0.60*/

  // Params: [269] [0.19258577118192985,0.5501102426246661]; fitness: 0.0135; avg: 0.019; stdDev: 0.002
  // For long string (x2)
  // For heuristic 2
  // In optimization: 27 ms - 45 ms
  // One shot: 44 ms - 55 ms
  // 400 - 600 rounds
  /*val PopulationSize: Int = 269
  val ElitismRate: Double = 0.1925
  val MutationRate: Double = 0.55*/

  // Params: [3] [0.4479206883628569,0.9395951580059205]; fitness: 0.0055; avg: 0.009; stdDev: 0.002
  // For long string (x2)
  // For heuristic 2
  // In optimization: 11 ms - 25 ms
  // One shot: 25 ms - 46 ms
  // 25k - 40k rounds
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
