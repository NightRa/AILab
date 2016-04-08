package string

import java.util.Random

import genetic._
import genetic.fitnessMapping.FitnessMapping
import genetic.generation.Crossover._
import genetic.generation.Generation
import genetic.localOptima.IgnoreLocalOptima
import genetic.mutation.RegularMutation
import genetic.selection.TopSelection
import genetic.survivors.Elitism
import parametric.Parametric
import params.{GeneticParamsMain, NamedParams, Params}

class GeneticStringMain(targetString: String,
                        crossover: (Array[Char], Array[Char], Random) => Array[Char],
                        heuristic: (Array[Char], Array[Char]) => Double) extends GeneticMetadata[Array[Char]] {
  val name: String = "String Search"
  val defaultMaxTime: Double = 10000
  val defaultPrintEvery = 100
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

  val target: Array[Char] = targetString.toCharArray
  def appliedHeuristic(state: Array[Char]) = heuristic(state, target)

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 3 // Minimum is best.
  )
  override def intsNamesMax: Map[String, Int] = Map(
    "Population Size" -> 20
  )
  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate"  -> 0.31, // Doesn't influence anything at all.
    "Gene Similarity Threshold" -> 0.485,
    "Local Optimum: Elitism Rate" -> 0.384,
    "Local Optimum: Hyper Mutation Rate" -> 1.0,
    "Local Optimum: Immigrants Rate" -> 0.41,
    "Local Optimum: Top Ratio" -> 0.356,
    "Mutation Rate" -> 0.52, // Very critical
    "Top Ratio" -> 0.55
  )

  override def genetic: Parametric[Genetic[Array[Char]]] =
    Parametric.point(new GeneticString(target, appliedHeuristic, crossover, rand))
}

object GeneticStringMain extends GeneticStringMain("Hello world! My name is Ilan.", onePointCrossoverString, StringHeuristics.heuristic2) with App {
  GeneticMain.runMain(this)
}

object GeneticStringOptimization extends GeneticParamsMain(GeneticStringMain.defaultGeneticAlgParametric, 300) with App {
  GeneticMain.runMain(this)
}
