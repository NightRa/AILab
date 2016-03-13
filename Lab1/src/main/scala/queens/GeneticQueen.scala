package queens

import java.util.Random

import genetic.Genetic

class GeneticQueen(MutationSize: Double,
                   rand: Random,
                   mateFunc : (Array[Int], Array[Int], Random) => Array[Int],
                   mutateFunc : (Array[Int], Random) => Unit) extends Genetic[QueenPermutation] {
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
}

