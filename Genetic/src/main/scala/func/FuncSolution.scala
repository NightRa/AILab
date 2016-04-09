package func

import java.lang.Math._
import java.util.Random

import func.Func._
import util.Util.formatScientific

abstract class Func(val minX: Double, val maxX: Double, val minY: Double, val maxY: Double) {
  val name: String

  // The function's minimum should be 0.
  protected def evaluate(x: Double, y: Double): Double

  protected def maxValue: Double

  def calc(solution: FuncSolution): Double =
    evaluate(toRange(solution.x, minX, maxX), toRange(solution.y, minY, maxY)) / maxValue
}

object Func {
  def toRange(v: Double, min: Double, max: Double): Double =
    v * (max - min) + min
}

object HoldersTableFunction extends Func(-10, 10, -10, 10) {
  val name: String = "Holder's Table Function"

  // The function's minimum should be 0.
  override protected def evaluate(x: Double, y: Double): Double =
    -abs(sin(x) * cos(y) * exp(abs(1 - sqrt(x * x + y * y) / PI))) + 19.2085

  override def maxValue: Double = 19.2085
}

object LabTestFunction extends Func(-10, 10, -10, 10) {
  val name: String = "Lab Test Function"

  // The function's minimum should be 0.
  override protected def evaluate(x: Double, y: Double): Double =
    20 + x * x + y * y - 10 * (cos(2 * PI * x) + cos(2 * PI * y))

  override def maxValue: Double = 20 + maxX * maxX + maxY * maxY + 20
}

case class FuncSolution(x: Double, y: Double, func: Func) {
  def xInRange = Func.toRange(x, func.minX, func.maxX)

  def yInRange = Func.toRange(y, func.minY, func.maxY)

  override def toString: String = {
    s"(${formatScientific(xInRange, 3)}, ${formatScientific(yInRange, 3)})"
  }
}

object FuncSolution {
  def genFuncSolution(func: Func, rand: Random): FuncSolution = {
    FuncSolution(
      rand.nextDouble(),
      rand.nextDouble(),
      func)
  }
}
