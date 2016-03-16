package func

import java.util.Random

import genetic._
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import genetic.types.{Gene, Population}
import params.{GeneticParamsMain, Params}

object GeneticFuncMain extends GeneticMain[Func] {
  val MaxTime: Double = 5.0

  // Params
  // Ints
  val PopulationSize: Int = 190 // 0
  // Doubles
  val ElitismRate: Double = 0.441 // 0
  val MutationRate: Double = 0.46 // 1
  val MutationSize: Double = 0.254 // 2
  val TopRatio: Double = 0.5 // 3

  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  def genetic(params: Params): Genetic[Func] = new GeneticFunc(params.doubles(2), rand)
  def mateStrategy(params: Params) = new ElitismMutationMateStrategy(params.doubles(0), params.doubles(1), rand)
  def selectionStrategy(params: Params) = new TopSelection(params.doubles(3))

  def initPopulation(params: Params) = new Population[Func](Array.fill(params.ints(0))(new Gene(Func.genFunc(rand), 0)))
  def initBuffer(params: Params) = new Population[Func](Array.fill(params.ints(0))(new Gene(Func(0, 0), 0)))

  def intsSize(): Int = 1
  def intsMax(): Int = 1024*16
  def doublesSize(): Int = 4

  def alg(params: Params, maxTime: Double): GeneticAlg[Func] =
    new GeneticAlg[Func](
      genetic(params), mateStrategy(params), selectionStrategy(params), maxTime,
      rand, initPopulation(params), initBuffer(params), _.toString)

  def main(args: Array[String]) {
    val start = System.currentTimeMillis()
    alg(new Params(Array(PopulationSize), Array(ElitismRate, MutationRate, MutationSize, TopRatio)), MaxTime).run(print = true)
    val end = System.currentTimeMillis()
    val time = end - start

    println(s"$time ms")
  }

}

object GeneticFuncOptimization extends GeneticParamsMain(GeneticFuncMain, 100)
