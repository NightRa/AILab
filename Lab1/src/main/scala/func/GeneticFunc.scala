package func

import java.util.Random

import genetic.Genetic
import util.Util._

class GeneticFunc(func: Func, MutationSize: Double, rand: Random) extends Genetic[FuncSolution] {

  def fitness(gene: FuncSolution): Double = func.calc(gene)

  def mate(x: FuncSolution, y: FuncSolution): FuncSolution = {
    FuncSolution(randAvg(x.x, y.x, rand), randAvg(x.y, y.y, rand))
  }

  def mutate(a: FuncSolution): FuncSolution = {
    val delta1 = (rand.nextFloat() - 0.5) * MutationSize * 2
    val delta2 = (rand.nextFloat() - 0.5) * MutationSize * 2
    FuncSolution(a.x + delta1 max 0 min 1, a.y + delta2 max 0 min 1)
  }
}
