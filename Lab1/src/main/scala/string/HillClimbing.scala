package string

import java.util.Random

class HillClimbing(heuristic: Array[Char] => Double, rand: Random) {
  val chars: Array[Char] = (32 to 91).map(_.toChar).toArray

  def hillClimbing(state: Array[Char]): String = {
    var index = 0
    while(heuristic(state) > 0) {
      val bestChar = chars.minBy(c => {
        state(index) = c
        heuristic(state)
      })
      state(index) = bestChar
      index += 1
    }
    state.mkString
  }

}
