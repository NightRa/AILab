package knapsack

import java.util.Random

import genetic.mating.{Crossover, ElitismMutationMateStrategy}
import genetic.selection.TopSelection
import genetic.types.{Gene, Population}
import genetic.{GeneticAlg, GeneticMain}
import params.{GeneticParamsMain, Params}
import string.{GeneticString, StringHeuristics}
import util.Util

object GeneticKnapsackMain extends GeneticMain[KnapsackElement] {

  val MaxTime: Double = 1
  // 9 ms - 20 ms
  // Params: [7] [0.16854000542619385,0.8595944684625508,0.538202072349739,0.7917486269698155]; fitness: 0.00958499451726917;

  // Parameters
  // Ints
  val DefaultPopulationSize: Int = 7 // 0
  // Doubles
  val DefaultElitismRate: Double = 0.1685 // 0
  val DefaultMutationRate: Double = 0.8595 // 1
  // For each value, change (+1) with bernuli(mutationProb)
  val DefaultMutationProb: Double = 0.5382 // 2
  val DefaultTopRatio: Double = 0.7917 // 3

  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)


  val items = Array(Item(0.22, 0.53), Item(0.33, 0.77), Item(0.1, 0.1), Item(1.2, 3), Item(1.5, 4)).sortBy(_.weight)
  val knapsackInstance = new KnapsackInstance(items, 10.0001) {
    // May override to optimal for testing.
    override def valueUpperBound: Double = 26.36
  }

  def intsSize(): Int = 1
  def intsMax(): Int = 4096
  def doublesSize(): Int = 4

  def alg(params: Params, maxTime: Double): GeneticAlg[KnapsackElement] = {
    val PopulationSize: Int = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val MutationProb = params.doubles(2)
    val TopRatio = params.doubles(3)

    val genetic = new GeneticKnapsack(Crossover.onePointCrossover, KnapsackMutate.binomialMutate(MutationProb, _, _), rand)

    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)
    def initPopulation() = new Population[KnapsackElement](Array.fill(PopulationSize)(new Gene(KnapsackElement.randomKnapsack(knapsackInstance, rand), 0)))
    def initBuffer() = new Population[KnapsackElement](Array.fill(PopulationSize)(new Gene(new KnapsackElement(Array.emptyIntArray, knapsackInstance), 0)))



    new GeneticAlg(
      genetic, mateStrategy, selectionStrategy, maxTime,
      rand, initPopulation(), initBuffer(), _.toString)
  }


  def main(args: Array[String]) {
    val start = System.currentTimeMillis()
    val (pop, iterations) = alg(new Params(Array(DefaultPopulationSize), Array(DefaultElitismRate, DefaultMutationRate, DefaultMutationProb, DefaultTopRatio)), MaxTime)
      .run(print = false)
    val end = System.currentTimeMillis()
    val time = end - start
    println(s"$time ms, $iterations iterations")
  }
}

object KnapsackOptimization extends GeneticParamsMain(GeneticKnapsackMain, 100)
