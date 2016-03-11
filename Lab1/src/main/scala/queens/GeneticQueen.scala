package queens

import java.util.Random

import genetic.Genetic

class GeneticQueen(MutationSize: Double, rand: Random) extends Genetic[QueenPermutation] {
  override def fitness(gene: QueenPermutation): Double = {
      QueensHeuristic.queensHeuristic(gene)
  }

  override def mate(x: QueenPermutation, y: QueenPermutation): QueenPermutation = ???

  override def mutate(a: QueenPermutation): QueenPermutation = ???
}

