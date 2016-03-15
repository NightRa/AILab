package knapsack

/**
  * Invariant: items sorted by weight.
  **/
class KnapsackInstance(val items: Array[Item], val capacity: Double) {
  // May override to optimal for testing.
  def valueUpperBound: Double = {
    val maxRatio = items.iterator.map(_.valueRatio).max
    capacity * maxRatio
  }
}
