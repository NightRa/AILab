package knapsack


class KnapsackInstance(val items: Array[Item], val sackSize: Double, val amounts: Array[Int]) {
  lazy val isValid: Boolean = totalAmount < sackSize

  lazy val totalAmount: Double = {
    items.iterator.zip(amounts.iterator).map { case (i, a) => i.price * a }.sum
  }

  lazy val totalMaxPrice: Double = {
    items.iterator.map(i => i.price * (sackSize / i.weight)).sum
  }

  lazy val percentLeft: Double = {
    1 - totalAmount / totalMaxPrice
  }
}
