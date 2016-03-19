package func

import java.lang.Math._
import java.util.Random

import Func._
import util.Util.formatScientific

abstract class Func(val minX: Double, val maxX: Double, val minY: Double, val maxY: Double) {
  val name: String

  // The function's minimum should be 0.
  protected def evaluate(x: Double, y: Double): Double

  def calc(solution: FuncSolution): Double =
    evaluate(toRange(solution.x, minX, maxX), toRange(solution.y, minY, maxY))
}

object Func {
  def toRange(v: Double, min: Double, max: Double): Double =
    v * (max - min) + min
}

object HoldersTableFunction extends Func(-10, 10, -10, 10) {
  val name: String = "Holder's Table Function"

  // The function's minimum should be 0.
  override protected def evaluate(x: Double, y: Double): Double =
    pow(1.5 - x + x * y, 2) + pow(2.25 - x + x * y * y, 2) + pow(2.625 - x + x * y * y * y, 2)
}

object LabTestFunction extends Func(-10, 10, -10, 10) {
  val name: String = "Lab Test Function"

  // The function's minimum should be 0.
  override protected def evaluate(x: Double, y: Double): Double =
    20 + x * x + y * y - 10 * (cos(2 * PI * x) + cos(2 * PI * y))
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
      toRange(rand.nextDouble(), func.minX, func.maxX),
      toRange(rand.nextDouble(), func.minY, func.maxY),
      func)
  }
}
