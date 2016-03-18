package queens

import util.JavaUtil

import java.util.Random

/**
  * Created by yuval on 3/11/2016.
  */
class QueenPermutation(val permutation : Array[Int]) {
  val size = permutation.length

  override def toString: String = permutation.mkString("[", ",", "]")
}

object QueenPermutation{
  def getPermutationRandomly(size : Int, rand: Random): QueenPermutation = {
    val array =  (1 to size).toArray
    JavaUtil.shuffleArray(array, rand)
    new QueenPermutation(array)
  }
}
