package knapsack

import scala.util.Random


class KnapsackInstance(val items: Array[Item], val sackSize: Double, val amounts: Array[Int]) {
  def isValid(): Boolean = totalAmount() < sackSize

  def totalAmount(): Double = {
    items.iterator.zip(amounts.iterator).map { case (i, a) => i.price * a }.sum
  }

  lazy val totalMaxPrice: Double = {
    items.iterator.map(i => i.price * (sackSize / i.weight)).sum
  }

  def percentLeft(): Double = {
    1 - totalAmount() / totalMaxPrice
  }

  def trim(rnd : Random) : Unit = {
    while (!this.isValid){
      val index = rnd.nextInt(amounts.length)
      if (amounts(index) > 0)
        amounts(index) = amounts(index) - 1
    }
  }
}
