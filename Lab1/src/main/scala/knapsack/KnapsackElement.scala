package knapsack

import scala.util.Random

class KnapsackElement(val amounts: Array[Int], val instance: KnapsackInstance) {
  def isValid(): Boolean = totalAmount() < instance.sackSize

  def totalAmount(): Double = {
    instance.items.iterator.zip(amounts.iterator).map { case (i, a) => i.price * a }.sum
  }

  def percentLeft(): Double = {
    1 - totalAmount() / instance.totalMaxPrice
  }

  def trim(rnd : Random) : Unit = {
    while (!this.isValid){
      val index = rnd.nextInt(amounts.length)
      if (amounts(index) > 0)
        amounts(index) = amounts(index) - 1
    }
  }
}
