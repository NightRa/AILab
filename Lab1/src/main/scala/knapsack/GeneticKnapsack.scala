package knapsack

import genetic.Genetic

import scala.util.Random

/**
  * Created by yuval on 3/15/2016.
  */
class GeneticKnapsack(rand : Random,
                      mateFunc : (KnapsackInstance, KnapsackInstance) => KnapsackInstance,
                      mutateFunc : KnapsackInstance => Unit) extends Genetic[KnapsackInstance]{
  override def fitness(gene: KnapsackInstance): Double = gene.percentLeft()

  override def mate(x: KnapsackInstance, y: KnapsackInstance): KnapsackInstance = mateFunc (x, y)

  override def mutate(a: KnapsackInstance): KnapsackInstance = {
    mutateFunc(a)
    a
  }
}
