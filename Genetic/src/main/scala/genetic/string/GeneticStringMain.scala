package genetic.string

import java.util.Random

import genetic.genetic.generation.Crossover._
import genetic.genetic.{Genetic, GeneticMain, GeneticMetadata}
import genetic.parametric.Parametric
import genetic.params.GeneticParamsMain

class GeneticStringMain(targetString: String,
                        crossover: (Array[Char], Array[Char], Random) => Array[Char],
                        heuristic: (Array[Char], Array[Char]) => Double) extends GeneticMetadata[Array[Char]] {
  val name: String = "String Search"
  val defaultMaxTime: Double = 1.0
  val defaultPrintEvery = 100

  val target: Array[Char] = targetString.toCharArray

  def appliedHeuristic(state: Array[Char]) = heuristic(state, target)

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 50
  )

  override def intsNamesMax: Map[String, Int] = Map(
    "Population Size" -> 256
  )

  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.12,
    "Gene Similarity Threshold" -> 0.3,
    "Local Optimum: Elitism Rate" -> 0.08,
    "Local Optimum: Hyper Mutation Rate" -> 0.94,
    "Local Optimum: Immigrants Rate" -> 0.23,
    "Local Optimum: Top Ratio" -> 0.2,
    "Mutation Rate" -> 0.36,
    "Top Ratio" -> 0.23
  )

  override def genetic: Parametric[Genetic[Array[Char]]] =
    Parametric.point(new GeneticString(target, appliedHeuristic, crossover, rand))
}

object GeneticStringMain extends GeneticStringMain("Hello world! My name is Ilan.", onePointCrossoverString, StringHeuristics.heuristic2) with App {
  GeneticMain.runMain(this)
}

object GeneticStringOptimization extends GeneticParamsMain(GeneticStringMain, GeneticStringMain.defaultGeneticAlgParametric, 300) with App {
  GeneticMain.runMain(this)
}
