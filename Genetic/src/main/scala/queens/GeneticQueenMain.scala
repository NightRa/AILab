package queens

import java.util.Random

import genetic.{Genetic, GeneticMain, GeneticMetadata}
import parametric.Parametric
import params.GeneticParamsMain

class GeneticQueenMain(boardSize: Int,
                       queenMating: (Array[Int], Array[Int], Random) => Array[Int],
                       queenMutation: (Array[Int], Random) => Unit) extends GeneticMetadata[QueenPermutation] {
  val name = "N-Queens"
  val defaultMaxTime: Double = 1.0

  override def defaultPrintEvery = 1

  override def genetic: Parametric[Genetic[QueenPermutation]] =
    Parametric.point {
      new GeneticQueen(boardSize, queenMating, queenMutation, rand)
    }

  // 8 ms - 10 ms

  // To be overwritten to provide problem-specific defaults.
  override def intNamesDefaults: Map[String, Int] = Map(
    "Population Size" -> 100
  )

  override def doubleNamesDefaults: Map[String, Double] = Map(
    "Elitism Rate" -> 0.1,
    "Mutation Rate" -> 0.4
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

object QueenOptimization extends GeneticParamsMain(GeneticQueenMain.defaultGeneticAlgParametric, 100) with App {
  GeneticMain.runMain(this)
}