package knapsack

import java.util.Random

import genetic.Genetic

class GeneticKnapsack(mateFunc: (Array[Int], Array[Int], Random) => Array[Int],
                      mutateFunc: (KnapsackElement, Random) => Unit,
                      rand: Random) extends Genetic[KnapsackElement] {
  override def fitness(gene: KnapsackElement): Double = gene.fitnessOfUppedBound()

  override def mate(x: KnapsackElement, y: KnapsackElement): KnapsackElement = {
    val childAmounts = mateFunc(x.amounts, y.amounts, rand)
    val child = new KnapsackElement(childAmounts, x.instance)
    child.trim(rand)
    child
  }

  override def mutate(a: KnapsackElement): KnapsackElement = {
    mutateFunc(a, rand)
    a
  }
}
