package knapsack

import genetic.Genetic

import scala.util.Random

class GeneticKnapsack(rand: Random,
                      mateFunc: (KnapsackElement, KnapsackElement) => KnapsackElement,
                      mutateFunc: KnapsackElement => Unit) extends Genetic[KnapsackElement] {
  override def fitness(gene: KnapsackElement): Double = gene.percentLeft()

  override def mate(x: KnapsackElement, y: KnapsackElement): KnapsackElement = mateFunc(x, y)

  override def mutate(a: KnapsackElement): KnapsackElement = {
    mutateFunc(a)
    a
  }
}
