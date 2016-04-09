package string

import java.util.Random

class HillClimbing(heuristic: Array[Char] => Double, print: Boolean, rand: Random) {
  val chars: Array[Char] = (32 to 126).map(_.toChar).toArray

  def hillClimbing(state: Array[Char]): String = {
    var index = 0
    while (heuristic(state) > 0 && index < state.length) {
      val bestChar = chars.minBy(c => {
        state(index) = c
        val value = heuristic(state)
        value
      })
      state(index) = bestChar
      index += 1
      if (print) println(state.mkString)
    }
    state.mkString
  }

}

object HillClimbing {
  def run(secret: String, heuristic: (Array[Char], Array[Char]) => Double, print: Boolean): Unit = {
    val TargetString: Array[Char] = secret.toCharArray

    val rand = new Random()
    val before = System.nanoTime()
    new HillClimbing(heuristic(_, TargetString), print, rand).hillClimbing(Array.ofDim[Char](secret.length))
    val after = System.nanoTime()
    val time = after - before
    val milis = (time / 1000).toDouble / 1000
    println(s"Time: $milis ms" + (if (print) " (including printing)" else ""))
  }
}
