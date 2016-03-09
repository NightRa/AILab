package func

import java.util.Random

import genetic.{Gene, GeneticAlg, OnePointCrossover, Population}

object GeneticFuncMain extends App {
  val PopulationSize: Int = 2048
  val MaxTime: Double = 5.0
  val ElitismRate: Float = 0.10f
  val MutationRate: Float = 0.2f
  val MutationSize: Float = 0.01f

  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  val alg = new GeneticFunc(MutationSize, rand)
  val mateStrategy = new OnePointCrossover(ElitismRate, MutationRate, rand)
  val geneticAlg = new GeneticAlg[Func](alg, mateStrategy, MaxTime, rand, _.toString)

  val population = new Population[Func](Array.fill(PopulationSize)(new Gene(Func.genFunc(rand), 0)))
  val buffer = new Population[Func](Array.fill(PopulationSize)(new Gene(Func(0, 0), 0)))

  geneticAlg.run(population, buffer, print = true)

}
