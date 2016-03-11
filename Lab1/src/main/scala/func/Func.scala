package func

import java.lang.Math._
import java.util.Random

case class Func(x1: Double, x2: Double) {
  def calc: Double = -abs(sin(x)*cos(y)*exp(abs(1-sqrt(x*x+y*y)/PI))) + 19.2085
  val x = x1*20-10
  val y = x2*20-10
  /*def calc: Double = pow(1.5 - x + x*y, 2) + pow(2.25-x+x*y*y, 2) + pow(2.625 - x + x*y*y*y, 2)
  val x = x1*9-4.5
  val y = x2*9-4.5*/
  // def calc: Double = Math.abs((x1 - 0.5) * (x2 - 0.5))
  def funcMax: Double = 0.25
  // def calc: Double = 20 + x1 * x1 + x2 * x2 - 10 * (cos(2 * PI * x1) + cos(2 * PI * x2))
  // def funcMax: Double = 42
}

object Func {
  def genFunc(rand: Random): Func = {
    Func(rand.nextDouble(), rand.nextDouble())
  }
}
