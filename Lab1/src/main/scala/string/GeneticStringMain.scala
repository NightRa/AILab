package string

import genetic._
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import params.{GeneticParamsMain, Params}
import util.Util

object GeneticStringMain extends GeneticMain[Array[Char]] {
  override val MaxTime: Double = 5.0
  override def fullOutput = true
  // Parameters
  // Ints
  val DefaultPopulationSize: Int = 3 // 0
  // Doubles
  val DefaultElitismRate: Double = 0.447 // 0
  val DefaultMutationRate: Double = 0.939 // 1
  val DefaultTopRatio: Double = 0.5 // 2
  override val intsMax: Int = 128
  override val defaultParams = Params(DefaultPopulationSize)(
    DefaultElitismRate, DefaultMutationRate, DefaultTopRatio)

  val targetString: Array[Char] = "Hello world! How are you doing today? My name is Ilan. Hello world! How are you doing today? My name is Ilan.".toCharArray

  def heuristic(state: Array[Char]) = StringHeuristics.heuristic2(state, targetString)

  def alg(params: Params, maxTime: Double): GeneticAlg[Array[Char]] = {
    val PopulationSize = params.ints(0)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)
    val TopRatio = params.doubles(2)

    val genetic = new GeneticString(heuristic, rand)
    val mateStrategy = new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
    val selectionStrategy = new TopSelection(MutationRate)

    new GeneticAlg(
      genetic, mateStrategy, selectionStrategy, PopulationSize,
      maxTime, rand,
      Util.randString(targetString.length, _),
      (arr: Array[Char]) => arr.mkString)
  }
}

object GeneticStringOptimization extends GeneticParamsMain(GeneticStringMain, 100, csv = false)
