package func

import java.lang.Math._
import java.util.Random

case class Func(x1: Double, x2: Double) {
  def calc: Double = 20 + x1 * x1 + x2 * x2 - 10 * (cos(2 * PI * x1) + cos(2 * PI * x2))
}

object Func {
  def genFunc(rand: Random): Func = {
    Func(rand.nextDouble(), rand.nextDouble())
  }
}
