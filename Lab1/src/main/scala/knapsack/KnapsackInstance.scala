package knapsack


class KnapsackInstance(val items: Array[Item], val sackSize: Double, val amounts: Array[Int]) {
  def totalAmount(): Double = {
    items.iterator.zip(amounts.iterator).map { case (i, a) => i.price * a }.sum
  }

  def totalMaxPrice() : Double = {
    items.iterator.map (i => i.price * (sackSize / i.weight)).sum
  }

  def percentLeft(): Double = {
    1 - totalAmount() / totalMaxPrice()
  }
}
