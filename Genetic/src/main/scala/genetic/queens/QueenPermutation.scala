package genetic.queens

import java.util.Random

import genetic.util.JavaUtil

case class QueenPermutation(permutation: Array[Int]) {
  val size = permutation.length

  override def toString: String = permutation.mkString("[", ",", "]")
}

object QueenPermutation {
  def getPermutationRandomly(size: Int, rand: Random): QueenPermutation = {
    val array = (1 to size).toArray
    JavaUtil.shuffleArray(array, rand)
    new QueenPermutation(array)
  }
}
