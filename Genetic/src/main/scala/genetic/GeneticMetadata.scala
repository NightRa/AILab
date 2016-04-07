package genetic

import java.util.Random

import genetic.generation.Generation
import genetic.localOptima.LocalOptimaSignal
import genetic.types.{Gene, Population}
import parametric.Instances._
import parametric.Parametric
import parametric.Parametric._

abstract class GeneticMetadata[A] {
  val seed = 8682522807148012L ^ System.nanoTime
  val rand: Random = new Random(seed)

  def name: String
  def defaultMaxTime: Double
  def defaultPrintEvery: Int
  def genetic: Parametric[Genetic[A]]

  // To be overwritten to provide problem-specific defaults.
  def intNamesDefaults: Map[String, Int] = Map.empty
  def intsNamesMax: Map[String, Int] = Map.empty
  def doubleNamesDefaults: Map[String, Double] = Map.empty


  def alg(localOptimaSignal: LocalOptimaSignal[A], normalGeneration: Generation[A], localOptimaGeneration: Generation[A]): Parametric[GeneticAlg[A]] =
    for {
      gen <- genetic
      popSize <- intParam("Population Size", default = 100, maxValue = 256)
    } yield new GeneticAlg[A](gen, localOptimaSignal, normalGeneration, localOptimaGeneration, popSize, rand)





























  // A SPECIFIC THING! Just for testing stuff I guess!
  def defaultGeneticAlgParametric: Parametric[GeneticAlg[A]] =
    (for {
      gen <- genetic
      localOptimaSignal <- geneSimilarity[A](gen)     // A SPECIFIC THING!
      normalGen <- normalGeneration[A]                // A SPECIFIC THING!
      localOptimaGen <- localOptimaGeneration[A](gen) // A SPECIFIC THING!
      geneticAlg <- alg(localOptimaSignal, normalGen, localOptimaGen)
    } yield geneticAlg).updateDefaults(intNamesDefaults, intsNamesMax, doubleNamesDefaults)
}

object GeneticMain {
  // Such as here!
  def runMain[A](geneticMeta: GeneticMetadata[A]): Unit = {
      val geneticAlg = geneticMeta.defaultGeneticAlgParametric.applyDefaults()

      val start: Long = System.currentTimeMillis
      val (population, iterations): (Population[A], Int) = geneticAlg.run(geneticMeta.defaultPrintEvery, geneticMeta.defaultMaxTime)

      val end: Long = System.currentTimeMillis
      val time: Long = end - start
      println(geneticMeta.defaultGeneticAlgParametric.toString)
      println("Best 5:")
      println(population.population.sorted(Ordering.by((x: Gene[A]) => x.fitness)).take(5).map(x => x.show(geneticAlg.genetic)).mkString("\n"))
      println(time + "ms, " + iterations + " iterations\t\t\t\tseed: " + geneticMeta.seed)

  }
}