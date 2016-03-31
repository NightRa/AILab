package knapsack

import java.util.Random

import genetic.{Metric, Genetic}
import util.Distance
import util.Distance._

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

  // TODO: Check whether working
  override def metric(): Metric[KnapsackElement] = new Metric[KnapsackElement] {
    override def distance(x: KnapsackElement, y: KnapsackElement): Double = {
      val instance = x.instance
      val capacity = instance.capacity
      def percentFull(index: Int, amounts: Array[Int]): Double = {
        val maxItems: Double = capacity / instance.items(index).weight
        amounts(index) / maxItems
      }
      def normalizedAmounts(amounts: Array[Int]): Array[Double] = {
        Array.tabulate(amounts.length)(i => percentFull(i, amounts))
      }
      val dist = arrayDistanceD(normalizedAmounts(x.amounts), normalizedAmounts(y.amounts))
      assert(dist >= 0 && dist <= 1)
      dist
    }
  }
}
