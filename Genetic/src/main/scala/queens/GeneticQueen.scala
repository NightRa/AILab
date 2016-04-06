package queens

import java.lang.Math.abs
import java.util.Random

import genetic.{Metric, Genetic}

class GeneticQueen(boardSize: Int,
                   mateFunc: (Array[Int], Array[Int], Random) => Array[Int],
                   mutateFunc: (Array[Int], Random) => Unit,
                   rand: Random) extends Genetic[QueenPermutation] {
  override def fitness(gene: QueenPermutation): Double = {
    QueensHeuristic.queensHeuristic(gene)
  }

  override def mate(x: QueenPermutation, y: QueenPermutation): QueenPermutation = {
    val son = mateFunc(x.permutation, y.permutation, rand)
    new QueenPermutation(son)
  }

  override def mutate(a: QueenPermutation): QueenPermutation = {
    mutateFunc(a.permutation, rand)
    a
  }

  override def metric(): Metric[QueenPermutation] = new Metric[QueenPermutation] {
    override def distance(x: QueenPermutation, y: QueenPermutation): Double = {
      var deltas = 0
      val length: Int = x.permutation.length
      for (i <- 0 until length) {
        val delta1 = abs(x.permutation(i) - x.permutation((i + 1) % length))
        val delta2 = abs(y.permutation(i) - y.permutation((i + 1) % length))
        deltas += abs(delta1 - delta2)
      }
      val res = deltas / ((length - 1) * length)
      assert(res >= 0 && res <= 1)
      res
    }
  }

  override def randomElement(rand: Random): QueenPermutation = QueenPermutation.getPermutationRandomly(boardSize, rand)

  override def show(gene: QueenPermutation): String = gene.permutation.mkString("[", ",", "]")
}

