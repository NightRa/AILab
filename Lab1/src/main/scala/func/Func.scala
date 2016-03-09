package func

import java.lang.Math._
import java.util.Random

case class Func(x1: Double, x2: Double) {
  def calc: Double = Math.abs((x1 - 0.5) * (x2 - 0.5))
  def funcMax: Double = 0.25
  // def calc: Double = 20 + x1 * x1 + x2 * x2 - 10 * (cos(2 * PI * x1) + cos(2 * PI * x2))
  // def funcMax: Double = 42
}

object Func {
  def genFunc(rand: Random): Func = {
    Func(rand.nextDouble(), rand.nextDouble())
  }
}
