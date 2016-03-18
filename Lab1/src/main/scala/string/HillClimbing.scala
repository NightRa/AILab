package string

import java.util.Random

import util.Util

class HillClimbing(heuristic: Array[Char] => Double, rand: Random) {
  val chars: Array[Char] = (32 to 126).map(_.toChar).toArray

  def hillClimbing(state: Array[Char]): String = {
    var index = 0
    while(heuristic(state) > 0 && index < state.length) {
      val bestChar = chars.minBy(c => {
        state(index) = c
        val value = heuristic(state)
        value
      })
      state(index) = bestChar
      index += 1
    }
    state.mkString
  }

}

object HillClimbing extends App {
  val TargetString: Array[Char] = "How are you? My name is Ilan.".toCharArray

  private val rand = new Random()
  val before = System.nanoTime()
  new HillClimbing(StringHeuristics.heuristic2(_, TargetString), rand).hillClimbing(Util.randString(TargetString.length, rand))
  val after = System.nanoTime()
  val time = after - before
  val milis = (time / 1000).toDouble / 1000
  println(s"Time: $milis ms")
}
