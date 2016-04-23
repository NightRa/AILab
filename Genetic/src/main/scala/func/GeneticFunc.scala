package func

import java.util.Random

import genetic.{Genetic, Metric}
import util.Distance
import util.Util._

class GeneticFunc(func: Func, MutationSize: Double, rand: Random) extends Genetic[FuncSolution] {

  def fitness(gene: FuncSolution): Double = func.calc(gene)

  def mate(v: FuncSolution, w: FuncSolution): FuncSolution = {
    FuncSolution(randAvg(v.x, w.x, rand), randAvg(v.y, w.y, rand), func)
  }

  def mutate(a: FuncSolution): FuncSolution = {
    val delta1 = (rand.nextFloat() - 0.5) * MutationSize * 2
    val delta2 = (rand.nextFloat() - 0.5) * MutationSize * 2
    FuncSolution(a.x + delta1 max 0 min 1, a.y + delta2 max 0 min 1, func)
  }

  override def metric(): Metric[FuncSolution] = new Metric[FuncSolution] {
    override def distance(x: FuncSolution, y: FuncSolution): Double = {
      Distance.euclidianDistance(x.xInRange, x.yInRange, y.xInRange, y.yInRange)
    }
  }

  override def randomElement(rand: Random): FuncSolution = FuncSolution.genFuncSolution(func, rand)

  override def show(funcSolution: FuncSolution): String = funcSolution.toString

  override def showScientific: Boolean = func.showScientific

  override def hash(gene: FuncSolution): Int = gene.hashCode()
}
