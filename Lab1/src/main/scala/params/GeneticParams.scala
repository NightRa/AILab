package params

import java.util.Random

import genetic.{Genetic, GeneticMain, Population}
import util.Util._

class GeneticParams(main: GeneticMain[_], IntsMutationSize: Int, DoublesMutationSize: Double, MutationRate: Double, TimeLimit: Double, rand: Random) extends Genetic[Params] {
  def fitness(gene: Params): Double = {
    val before: Long = System.currentTimeMillis()
    val (population: Population[_], iterations: Int) = main.alg(gene, TimeLimit).run(print = false)
    val after: Long = System.currentTimeMillis()
    val time = after - before
    println(s"$time ms" + (if(time > 990) gene.toString else ""))
    // Finished => [0,0.5], Not finished => [0.5,1]
    0.5 * time.toDouble / (TimeLimit * 1000) + 0.5 * population.population.minBy(_.fitness).fitness
  }

  def mate(a: Params, b: Params): Params = {
    val ints = zipWith(a.ints, b.ints)((x, y) => randAvg(x, y, rand).toInt)
    val doubles = zipWith(a.doubles, b.doubles)((x, y) => randAvg(x, y, rand))
    new Params(ints, a.intsMax, doubles)
  }

  def mutate(params: Params): Params = {
    for (i <- params.ints.indices) {
      if (rand.nextDouble() < MutationRate) {
        val delta = (rand.nextInt(IntsMutationSize) - (IntsMutationSize / 2)) * 2
        // max 2 so that population size / 2 isn't 0.
        params.ints(i) = (params.ints(i) + delta) max 3 min params.intsMax
      }
    }

    for (i <- params.doubles.indices) {
      if (rand.nextDouble() < MutationRate) {
        val delta = ((rand.nextDouble() * DoublesMutationSize) - (DoublesMutationSize / 2)) * 2
        params.doubles(i) = (params.doubles(i) + delta) max 0 min 1
      }
    }

    params
  }
}
