package string

import genetic._
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import params.{GeneticParamsMain, Params}
import util.Util

object GeneticStringMain extends GeneticMain[Array[Char]] {
  override val MaxTime: Double = 5.0

  override def fullOutput = true

  // 3.2 ms for Heuristic 1, Genetic Params.
  // Params: [3] [0.4155463088614557,0.6205792746313119,0.6534676794176206]
  // 3.12 ms, for heuristic 2, Genetic Params.
  // Params: [5] [0.317438506378625,0.569345044769295,0.8385242624071847]
  // 2.78 ms for heuristic 2, with Analysis.
  // Params: [3] [0.55, 0.65, 0.83]

  // For heuristic 3:
  // With Analysis: 1.8 ms
  // Params: [3] [0.63, 0.99, 0.5]

  // For heuristic 2, Uniform crossover:
  // Params: [480] [0.49, 0.49, 0.4]

  // Parameters
  // Minimum is best.
  val DefaultPopulationSize: Int = 3 // 0

  // Doesn't influence anything at all.
  val DefaultElitismRate: Double = 0.63
  // 0
  // Very critical
  val DefaultMutationRate: Double = 0.99
  // 1
  // Doesn't influence anything at all.
  val DefaultTopRatio: Double = 0.5
  // 2
  override val intsMax: Int = 1024
  override val defaultParams = Params(DefaultPopulationSize)(
    DefaultElitismRate, DefaultMutationRate, DefaultTopRatio)

  val targetString: Array[Char] = "How are you? My name is Ilan.".toCharArray

  def heuristic(state: Array[Char]) = StringHeuristics.heuristic3(state, targetString, 1, 2)

  def alg(params: Params, maxTime: Double): GeneticAlg[Array[Char]] = {
    val PopulationSize = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val TopRatio = params.doubles(2)

    val genetic = new GeneticString(heuristic, rand)
    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(TopRatio)

    new GeneticAlg(
      genetic, mateStrategy, selectionStrategy, PopulationSize,
      maxTime, rand,
      Util.randString(targetString.length, _),
      (arr: Array[Char]) => arr.mkString)
  }
}

object GeneticStringOptimization extends GeneticParamsMain(GeneticStringMain, 100, csv = false)
