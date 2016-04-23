package knapsack

import java.util.Random

import genetic.{Genetic, Metric}
import util.Distance._

class GeneticKnapsack(instance: KnapsackInstance,
                      mateFunc: (Array[Int], Array[Int], Random) => Array[Int],
                      mutateFunc: (KnapsackElement, Random) => Unit,
                      rand: Random) extends Genetic[KnapsackElement] {
  override def fitness(gene: KnapsackElement): Double = gene.fitnessUpperBound()

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
      def normalizedAmounts(amounts: Array[Int]): Iterator[Double] = {
        Iterator.tabulate(amounts.length)(i => percentFull(i, amounts))
      }
      // TODO: This 10000 is arbitrary, fix it.
      val dist = arrayDistanceD(normalizedAmounts(x.amounts), normalizedAmounts(y.amounts)) / 10000
      assert(dist >= 0 && dist <= 1)
      dist
    }
  }

  override def randomElement(rand: Random): KnapsackElement = KnapsackElement.randomKnapsack(instance, rand)

  override def show(gene: KnapsackElement): String = gene.toString

  override def showScientific: Boolean = false

  override def hash(gene: KnapsackElement): Int = gene.hashCode()
}
