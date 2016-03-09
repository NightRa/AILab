package func

import genetic.{Genetic, Population}

import scala.util.Random

class GeneticFunc(MutationSize: Float, rand: Random) extends Genetic[Func] {
  def fitness(gene: Func): Double = gene.calc

  def mate(x: Func, y: Func): Func = {
    val w1 = rand.nextFloat()
    val w2 = rand.nextFloat()
    Func(x.x1 * w1 + y.x1 * (1 - w1), x.x2 * w2 + y.x2 * (1 - w2))
  }

  def mutate(a: Func): Func = {
    val delta1 = (rand.nextFloat() - 0.5) * MutationSize
    val delta2 = (rand.nextFloat() - 0.5) * MutationSize
    Func(a.x1 + delta1, a.x2 + delta2)
  }
}
