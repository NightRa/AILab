package string

import java.util.Random

import util.Util

class HillClimbing(heuristic: Array[Char] => Double, rand: Random) {
  val chars: Array[Char] = (32 to 91).map(_.toChar).toArray

  def hillClimbing(state: Array[Char]): String = {
    var index = 0
    while(heuristic(state) > 0 && index < state.length) {
      val bestChar = chars.minBy(c => {
        state(index) = c
        val value = heuristic(state)
        value
      })
      state(index) = bestChar
      println(s"Index $index, best char = $bestChar: " + state.take(index + 1).mkString)
      index += 1
    }
    state.mkString
  }

}

object HillClimbing extends App {
  val TargetString: Array[Char] = "Hello world! How are you doing today? My name is Ilan.".toCharArray

  private val rand = new Random()
  new HillClimbing(StringHeuristics.heuristic2(_, TargetString), rand).hillClimbing(Util.randString(TargetString.length, rand))
}
