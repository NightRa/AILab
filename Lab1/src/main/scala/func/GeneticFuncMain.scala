package func

import java.util.Random

import genetic._
import params.{GeneticParamsMain, Params}

object GeneticFuncMain extends GeneticMain[Func] {
  val MaxTime: Double = 5.0

  // 40 ms - 55 ms
  /*val PopulationSize: Int = 2048
  val ElitismRate: Double = 0.10f
  val MutationRate: Double = 0.2f
  val MutationSize: Double = 0.01f*/

  // 12 ms - 18 ms
  /*val PopulationSize: Int = 200
  val ElitismRate: Double = 0.83
  val MutationRate: Double = 0.70
  val MutationSize: Double = 0.86*/

  // Params: [168] [0.07826891264575286,0.309110118057211,0.11850811360304636]; fitness: 3.0409567777956366E-13; avg: 0.002; stdDev: 0.001
  // 7 ms - 8 ms
  val PopulationSize: Int = 168
  val ElitismRate: Double = 0.078
  val MutationRate: Double = 0.309
  val MutationSize: Double = 0.118

  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  def genetic(params: Params): Genetic[Func] = new GeneticFunc(params.doubles(2), rand)
  def mateStrategy(params: Params) = new OnePointCrossover(params.doubles(0), params.doubles(1), rand)

  def initPopulation(params: Params) = new Population[Func](Array.fill(params.ints(0))(new Gene(Func.genFunc(rand), 0)))
  def initBuffer(params: Params) = new Population[Func](Array.fill(params.ints(0))(new Gene(Func(0, 0), 0)))

  def intsSize(): Int = 1
  def intsMax(): Int = 1024*16
  def doublesSize(): Int = 3

  def alg(params: Params, maxTime: Double): GeneticAlg[Func] =
    new GeneticAlg[Func](genetic(params), mateStrategy(params), maxTime, rand, initPopulation(params), initBuffer(params), _.toString)

  def main(args: Array[String]) {
    val start = System.currentTimeMillis()
    alg(new Params(Array(PopulationSize), intsMax(), Array(ElitismRate, MutationRate, MutationSize)), MaxTime).run(print = true)
    val end = System.currentTimeMillis()
    val time = end - start

    println(s"$time ms")
  }

}

object GeneticFuncOptimization extends GeneticParamsMain(GeneticFuncMain)
