package string

import java.util.Random

import genetic._
import genetic.localOptima.IgnoreLocalOptima
import genetic.mating.Crossover._
import genetic.mating.{Crossover, ElitismMutationMateStrategy}
import genetic.selection.TopSelection
import params.{GeneticParamsMain, NamedParams, Params}
import util.Util

class GeneticStringMain(targetString: String,
                        crossover: (Array[Char], Array[Char], Random) => Array[Char],
                        heuristic: (Array[Char], Array[Char]) => Double) extends GeneticMain[Array[Char]] {
  val name: String = "String Search"
  val MaxTime: Double = 5.0
  val printEvery = 100
  /*
    3.2 ms for Heuristic 1, Genetic Params.
    Params: [3] [0.4155463088614557,0.6205792746313119,0.6534676794176206]

    3.12 ms, for heuristic 2, Genetic Params.
    Params: [5] [0.317438506378625,0.569345044769295,0.8385242624071847]

    2.78 ms for heuristic 2, with Analysis.
    Params: [3] [0.55, 0.65, 0.83]

    1.8 ms for heuristic 3, With Analysis.
    Params: [3] [0.63, 0.99, 0.5]

    For heuristic 2, Uniform crossover:
    Params: [480] [0.49, 0.49, 0.4]
  */

  override val intsMax: Int = 1024
  override val defaultParams = NamedParams(
    "Population Size" -> 3 // Minimum is best.
  )(
    "Elitism Rate" -> 0.63, // Doesn't influence anything at all.
    "Mutation Rate" -> 0.99, // Very critical
    "Top Ratio" -> 0.5 // Doesn't influence anything at all.
  )
  val target: Array[Char] = targetString.toCharArray

  def appliedHeuristic(state: Array[Char]) = heuristic(state, target)

  def alg(params: Params, maxTime: Double): GeneticAlg[Array[Char]] = {
    val PopulationSize = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val TopRatio = params.doubles(2)

    val genetic = new GeneticString(appliedHeuristic, crossover, rand)
    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)
    val localOptimaDetector = new IgnoreLocalOptima[Array[Char]]()

    new GeneticAlg(
      genetic, mateStrategy, selectionStrategy, localOptimaDetector,
      PopulationSize,
      maxTime, rand,
      Util.randString(targetString.length, _),
      (arr: Array[Char]) => arr.mkString)
  }
}

object GeneticStringMain extends GeneticStringMain("Hello world! My name is Ilan.", onePointCrossoverString, StringHeuristics.heuristic2)

object GeneticStringOptimization extends GeneticParamsMain(GeneticStringMain, 100)
