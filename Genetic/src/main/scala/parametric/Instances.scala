package parametric

import java.util.Random

import genetic.fitnessMapping.{Aging, Niching, Windowing}
import genetic.generation.Generation
import genetic.localOptima.{DistanceSimilarityDetector, LocalOptimaSignal, StdDevLocalOptimaDetector}
import genetic.mutation.RegularMutation
import genetic.selection.{RouletteWheelSelection, TopSelection}
import genetic.survivors.{Elitism, ElitismRandomImmigrants}
import genetic.{Genetic, GeneticAlg, GeneticEngine}
import parametric.Parametric._

object Instances {
  def niching: Parametric[Niching] =
    for {
      alpha <- doubleParam("Niching alpha", 1)
      sigmaShare <- doubleParam("Niching sigma share", 0.01)
    } yield new Niching(alpha, sigmaShare)

  def geneSimilarity: Parametric[DistanceSimilarityDetector] =
    for {
      distanceThresh <- doubleParam("Gene Similarity Threshold", 0.5)
    } yield new DistanceSimilarityDetector(distanceThresh)

  def stdDevSimilarity: Parametric[StdDevLocalOptimaDetector] =
    for {
      stdDevThreshold <- doubleParam("Std. Dev. Similarity Threshold", 0.01)
    } yield new StdDevLocalOptimaDetector(stdDevThreshold)

  def elitism: Parametric[Elitism] =
    for {
      elitismRate <- doubleParam("Elitism Rate", 0.5)
    } yield new Elitism(elitismRate)

  def randomImmigrantsElitism[A]: Parametric[ElitismRandomImmigrants] =
    for {
      elitismRate <- doubleParam("Elitism Rate", 0.0)
      randomImmigrantsRate <- doubleParam("Immigrants Rate", 0.5)
    } yield new ElitismRandomImmigrants(randomImmigrantsRate, elitismRate)

  def topSelection: Parametric[TopSelection] =
    for {
      topRatio <- doubleParam("Top Ratio", 0.8)
    } yield new TopSelection(topRatio)

  def rws: Parametric[RouletteWheelSelection] =
    Parametric.point {
      new RouletteWheelSelection()
    }

  def mutation: Parametric[RegularMutation] =
    for {
      mutationRate <- doubleParam("Mutation Rate", 0.3)
    } yield new RegularMutation(mutationRate)

  def hyperMutation: Parametric[RegularMutation] =
    for {
      mutationRate <- doubleParam("Hyper Mutation Rate", 1)
    } yield new RegularMutation(mutationRate)

  def windowing: Parametric[Windowing] =
    Parametric.point {
      new Windowing()
    }

  def aging: Parametric[Aging] =
    for {
      matureAge <- intParam("Maturity Age", default = 5, minValue = 0, maxValue = 20)
      agingInfluence <- doubleParam("Aging influence", 0.3)
    } yield new Aging(matureAge, agingInfluence)

  def defaultNormalGeneration: Parametric[Generation] =
    for {
      selectionStrategy <- topSelection
      mutationStrategy <- mutation
      survivorSelection <- elitism
    //  windowing <- windowing[A]
    // aging <- aging[A]
    } yield new Generation(selectionStrategy, mutationStrategy, survivorSelection, Array(/*windowing *//*aging*/))

  def defaultLocalOptimaGeneration: Parametric[Generation] = {
    val prefix = "Local Optimum: "
    (for {
      selectionStrategy <- topSelection
      mutationStrategy <- hyperMutation
      survivorSelection <- randomImmigrantsElitism
    // windowing <- windowing[A]
    // aging <- aging[A]
    // niching <- niching(alg)
    } yield new Generation(selectionStrategy, mutationStrategy, survivorSelection, Array(/*windowing *//*aging*//*niching*/))).prefixed(prefix)
  }


  def geneticEngine(localOptimaSignal: LocalOptimaSignal, normalGeneration: Generation, localOptimaGeneration: Generation): Parametric[GeneticEngine] =
    for {
      popSize <- intParam("Population Size", default = 100, minValue = 3, maxValue = 256)
    } yield new GeneticEngine(localOptimaSignal, normalGeneration, localOptimaGeneration, popSize)

  def defaultGeneticEngine: Parametric[GeneticEngine] =
    for {
      localOptimaSignal <- geneSimilarity
      normalGeneration <- defaultNormalGeneration
      localOptimumGeneration <- defaultLocalOptimaGeneration
      engine <- geneticEngine(localOptimaSignal, normalGeneration, localOptimumGeneration)
    } yield engine

  def geneticAlg[A](genetic: Parametric[Genetic[A]], engine: Parametric[GeneticEngine], rand: Random): Parametric[GeneticAlg[A]] =
    for {
      gen <- genetic
      geneticEngine <- engine
    } yield new GeneticAlg[A](gen, geneticEngine, rand)

  def defaultGeneticAlg[A](genetic: Parametric[Genetic[A]], rand: Random): Parametric[GeneticAlg[A]] =
    geneticAlg(genetic, defaultGeneticEngine, rand)


}
