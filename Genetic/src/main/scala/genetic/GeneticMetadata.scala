package genetic

import genetic.types.{Gene, Population}
import it.unimi.dsi.Util
import it.unimi.dsi.util.XorShift128PlusRandom
import parametric.Instances._
import parametric.Parametric

abstract class GeneticMetadata[A] {
  val seed = Util.randomSeed()
  val rand: XorShift128PlusRandom = new XorShift128PlusRandom(seed)

  def name: String
  def defaultMaxTime: Double
  def defaultPrintEvery: Int
  def genetic: Parametric[Genetic[A]]
  def isOpt: Boolean = false

  // To be overwritten to provide problem-specific defaults.
  def intNamesDefaults: Map[String, Int] = Map.empty
  def intsNamesMax: Map[String, Int] = Map.empty
  def doubleNamesDefaults: Map[String, Double] = Map.empty

  def alg(engine: Parametric[GeneticEngine]): Parametric[GeneticAlg[A]] =
    geneticAlg(genetic, engine, rand).updateDefaults(intNamesDefaults, intsNamesMax, doubleNamesDefaults)

  def defaultGeneticAlgParametric: Parametric[GeneticAlg[A]] =
    defaultGeneticAlg(genetic, rand).updateDefaults(intNamesDefaults, intsNamesMax, doubleNamesDefaults)
}

object GeneticMain {
  // A main with the default genetic algorithm engine
  def runMain[A](geneticMeta: GeneticMetadata[A]): Unit = {
    val geneticAlg = geneticMeta.defaultGeneticAlgParametric.applyDefaults()

    val start: Long = System.currentTimeMillis
    val (population, iterations): (Population[A], Int) = geneticAlg.run(geneticMeta.defaultPrintEvery, geneticMeta.defaultMaxTime)

    val end: Long = System.currentTimeMillis
    val time: Long = end - start
    println(geneticMeta.defaultGeneticAlgParametric.toString)
    println("Best 5:")
    println(population.population.sorted(Ordering.by((x: Gene[A]) => x.fitness)).take(5).map(x => x.show(geneticMeta.genetic.applyDefaults())).mkString("\n"))
    println(time + "ms, " + iterations + " iterations\t\t\t\tseed: " + geneticMeta.seed)

  }
}