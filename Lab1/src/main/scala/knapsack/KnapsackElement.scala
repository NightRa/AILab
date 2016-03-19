package knapsack

import java.util.Random

import util.JavaUtil
import util.JavaUtil.formatDouble

class KnapsackElement(val amounts: Array[Int], val instance: KnapsackInstance) {
  def isValid(): Boolean = totalWeight() < instance.capacity

  def totalWeight(): Double = {
    instance.items.iterator.zip(amounts.iterator).map { case (i, a) => i.weight * a }.sum
  }

  def totalValue(): Double = {
    instance.items.iterator.zip(amounts.iterator).map { case (i, a) => i.value * a }.sum
  }

  def fitnessOfUppedBound(): Double = {
    1 - totalValue() / instance.valueUpperBound
  }

  def trim(rnd: Random): Unit = {
    while (!this.isValid) {
      val index = rnd.nextInt(amounts.length)
      if (amounts(index) > 0)
        amounts(index) = amounts(index) - 1
    }
  }

  override def toString: String = {
    "Weight:" + formatDouble(totalWeight(), 2) + ",\tValue: " + formatDouble(totalValue(), 2) + ", " + amounts.iterator.zipWithIndex.map {
      case (amount, i) =>
        val item = instance.items(i)
        "(" + item.weight + "," + item.value + ")" + "->" + amount
    }.mkString("[", ", ", "]")
  }
}

object KnapsackElement {
  def randomKnapsack(instance: KnapsackInstance, rand: Random): KnapsackElement = {
    var weight = 0.0
    val numItems = instance.items.length
    val amounts = Array.fill(numItems)(0)
    val minWeight = instance.items(0).weight
    while (weight <= instance.capacity && (instance.capacity - weight >= minWeight)) {
      val item = rand.nextInt(numItems)
      amounts(item) += 1
      weight += instance.items(item).value
    }

    val knapsack = new KnapsackElement(amounts, instance)
    knapsack.trim(rand)
    knapsack
  }
}
