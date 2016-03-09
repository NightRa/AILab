package func

import java.util.Random

import genetic.Genetic
import util.Util._

class GeneticFunc(MutationSize: Double, rand: Random) extends Genetic[Func] {

  def fitness(gene: Func): Double = gene.calc

  def mate(x: Func, y: Func): Func = {
    Func(randAvg(x.x1, y.x1, rand), randAvg(x.x2, y.x2, rand))
  }

  def mutate(a: Func): Func = {
    val delta1 = (rand.nextFloat() - 0.5) * MutationSize * 2
    val delta2 = (rand.nextFloat() - 0.5) * MutationSize * 2
    Func(a.x1 + delta1 max 0 min 1, a.x2 + delta2 max 0 min 1)
  }
}
