package genetic.queens

import java.util.Random

import genetic.genetic.{Genetic, GeneticMain, GeneticMetadata}
import genetic.genetic.GeneticMain
import genetic.parametric.Parametric
import genetic.params.GeneticParamsMain

class GeneticQueenMain(boardSize: Int,
                       queenMating: (Array[Int], Array[Int], Random) => Array[Int],
                       queenMutation: (Array[Int], Random) => Unit) extends GeneticMetadata[QueenPermutation] {
  val name = "N-Queens"
  val defaultMaxTime: Double = 1.0

  override def defaultPrintEvery = 10

  override def genetic: Parametric[Genetic[QueenPermutation]] =
    Parametric.point {
      new GeneticQueen(boardSize, queenMating, queenMutation, rand)
    }

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 6
  )

  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.161,
    "Gene Similarity Threshold" -> 0.526,
    "Local Optimum: Elitism Rate" -> 0.039,
    "Local Optimum: Hyper Mutation Rate" -> 0.63,
    "Local Optimum: Immigrants Rate" -> 0.083,
    "Local Optimum: Top Ratio" -> 0.089,
    "Mutation Rate" -> 0.743,
    "Top Ratio" -> 0.761
  )
}

object GeneticQueenMain extends GeneticQueenMain(10, QueenMating.pmx, QueenMutation.exchange) {
  def showBoard(permutation: Array[Int]): String = {
    val size = permutation.length
    permutation.map(i => "- " * (i - 1) + "x " + "- " * (size - i)).mkString("\n", "\n", "\n")
  }

  def main(args: Array[String]) {
    GeneticMain.runMain(this)
  }
}

object QueenOptimization extends GeneticParamsMain(GeneticQueenMain, GeneticQueenMain.defaultGeneticAlgParametric, 100) with App {
  GeneticMain.runMain(this)
}