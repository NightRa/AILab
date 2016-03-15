package knapsack

/**
  * Created by yuval on 3/15/2016.
  */
class KnapsackInstance(val items: Array[Item], val sackSize: Double, val amounts: Array[Int]) {
  def totalAmount(): Double = {
    items.iterator.zip(amounts.iterator).map { case (i, a) => i.weight * a }.sum
  }

  def percentLeft(): Double = {
    1 - totalAmount() / sackSize
  }

}
