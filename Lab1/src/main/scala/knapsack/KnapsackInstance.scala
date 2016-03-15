package knapsack

import scala.util.Random


class KnapsackInstance(val items: Array[Item], val sackSize: Double) {
  lazy val totalMaxPrice: Double = {
    items.iterator.map(i => i.price * (sackSize / i.weight)).sum
  }
}
