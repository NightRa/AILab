package genetic.knapsack

import java.util

/**
  * Invariant: items sorted by weight.
  **/
class KnapsackInstance(val items: Array[Item], val capacity: Double) {
  // May override to optimal for testing.
  def valueUpperBound: Double = {
    val maxRatio = items.iterator.map(_.valueRatio).max
    capacity * maxRatio
  }


  def canEqual(other: Any): Boolean = other.isInstanceOf[KnapsackInstance]

  override def equals(other: Any): Boolean = other match {
    case that: KnapsackInstance =>
      (that canEqual this) &&
        (items sameElements that.items) &&
        capacity == that.capacity
    case _ => false
  }

  override def hashCode(): Int = {
    util.Arrays.deepHashCode(items.asInstanceOf[Array[Object]]) * 31 + java.lang.Double.hashCode(capacity)
  }
}
