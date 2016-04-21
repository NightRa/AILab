package mdKnapsack

import java.util.Random

import util.BitSet

case class Sack(capacity: Int, itemWeights: Array[Int]) {
  def weightOfItems(items: BitSet): Int = {
    var sum = 0
    val cloned_items = items.clone()
    var i: Int = cloned_items.lowestBit()
    while(i != -1) {
      sum += itemWeights(i)
      cloned_items.clear(i)
      i = cloned_items.lowestBit()
    }
    sum
  }

  def fits(items: BitSet): Boolean = {
    weightOfItems(items) <= capacity
  }

  override def toString: String = s"Sack: capacity = $capacity, itemWeights: " + itemWeights.mkString("[", ",", "]")
}

case class MDKnapsackInstance(name: String, values: Array[Int], sacks: Array[Sack], optimum: Int) {
  // TODO: Efficient Implementation
  def trim(items: BitSet, rand: Random) = {
    val size = values.length
    while(!sacks.forall(_.fits(items))) {
      val i = rand.nextInt(size)
      items.clear(i)
    }
    items
  }

  def value(chosen: BitSet): Int = {
    var i = 0
    var sum = 0
    val length = values.length
    while (i < length) {
      if(chosen.get(i)) sum += values(i)
      i += 1
    }
    sum
  }

  override def toString: String =
    s"MDKnapsackInstance: $name" +
      s"\n\tOptimum: $optimum" +
      s"\n\tvalues: ${values.mkString("[", ",", "]")}" +
      s"\n\tsacks: " + sacks.mkString("\n\t\t", "\n\t\t", "")
}
