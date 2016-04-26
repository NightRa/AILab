package genetic.mdKnapsack

import java.util.Random

import genetic.util.{BitSet, IntBuffer}

case class Sack(capacity: Int, itemWeights: Array[Int]) {
  override def toString: String = s"Sack: capacity = $capacity, itemWeights: " + itemWeights.mkString("[", ",", "]")
}

case class MDKnapsackInstance(name: String, values: Array[Int], sacks: Array[Sack], optimum: Int) {

  // @Requires a proof of ownership for the IntBuffer
  def trim(items: BitSet, itemsBuffer: IntBuffer, rand: Random): BitSet = {
    MDKnapsack.trim(items, sacks, itemsBuffer, rand)
    items
  }

  // @Requires a proof of ownership for the IntBuffer
  def value(chosen: BitSet, itemsBuffer: IntBuffer): Int = {
    MDKnapsack.takenItems(chosen, itemsBuffer)
    MDKnapsack.valueOfItems(itemsBuffer, values) // proof: we just calculated takenItems.
  }

  override def toString: String =
    s"MDKnapsackInstance: $name" +
      s"\n\tOptimum: $optimum" +
      s"\n\tvalues: ${values.mkString("[", ",", "]")}" +
      s"\n\tsacks: " + sacks.mkString("\n\t\t", "\n\t\t", "")
}
