package genetic.parametric

import java.util.Random

import genetic.genetic.{Genetic, GeneticAlg, GeneticEngine}
import genetic.genetic.fitnessMapping._
import genetic.genetic.generation.Generation
import genetic.genetic.localOptima.{DistanceSimilarityDetector, IgnoreLocalOptima, LocalOptimaSignal, StdDevLocalOptimaDetector}
import genetic.genetic.mutation.RegularMutation
import genetic.genetic.selection._
import genetic.genetic.survivors.construction.NormalConstruction
import genetic.genetic.survivors.{Elitism, RandomImmigrants}
import genetic.genetic.GeneticEngine
import genetic.parametric.Parametric._

object Instances {
  def niching: Parametric[Niching] =
    for {
      alpha <- doubleParam("Niching alpha", 1)
      sigmaShare <- doubleParam("Niching sigma share", 0.01)
    } yield new Niching(alpha, sigmaShare)

  def geneSimilarity: Parametric[DistanceSimilarityDetector] =
    for {
      distanceThresh <- doubleParam("Gene Similarity Threshold", 0.3)
    } yield new DistanceSimilarityDetector(distanceThresh)

  def stdDevSimilarity: Parametric[StdDevLocalOptimaDetector] =
    for {
      stdDevThreshold <- doubleParam("Std. Dev. Similarity Threshold", 0.01)
    } yield new StdDevLocalOptimaDetector(stdDevThreshold)

  def ignoreLocalOptima: Parametric[IgnoreLocalOptima] =
    Parametric.point {
      new IgnoreLocalOptima()
    }

  def elitism: Parametric[Elitism] =
    for {
      elitismRate <- doubleParam("Elitism Rate", 0.5)
    } yield new Elitism(elitismRate)

  def randomImmigrantsElitism: Parametric[RandomImmigrants] =
    for {
      elitismRate <- doubleParam("Elitism Rate", 0.1)
      randomImmigrantsRate <- doubleParam("Immigrants Rate", 0.3)
    } yield new RandomImmigrants(randomImmigrantsRate, elitismRate)

  def topSelection: Parametric[TopSelection] =
    for {
      topRatio <- doubleParam("Top Ratio", 0.8)
    } yield new TopSelection(topRatio)

  def rws: Parametric[RouletteWheelSelection] =
    Parametric.point {
      new RouletteWheelSelection()
    }

  def sus: Parametric[StochasticUniversalSampling] =
    Parametric.point {
      new StochasticUniversalSampling()
    }

  def ranking: Parametric[Ranking] =
    Parametric.point {
      new Ranking()
    }

  def tournament: Parametric[Tournament] =
    for {
      tournamentSize <- intParam("Tournament Size", default = 4, minValue = 1, maxValue = 20)
    } yield new Tournament(tournamentSize)

  def mutation: Parametric[RegularMutation] =
    for {
      mutationRate <- doubleParam("Mutation Rate", 0.3)
    } yield new RegularMutation(mutationRate)

  def hyperMutation: Parametric[RegularMutation] =
    for {
      mutationRate <- doubleParam("Hyper Mutation Rate", 0.8)
    } yield new RegularMutation(mutationRate)

  def windowing: Parametric[Windowing] =
    Parametric.point {
      new Windowing()
    }

  def exponentialScaling: Parametric[ExponentialScaling] =
    Parametric.point {
      new ExponentialScaling()
    }

  def sigmaScaling: Parametric[SigmaScaling] = {
    Parametric.point {
      new SigmaScaling()
    }
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
      elitism <- elitism
    //  windowing <- windowing[A]
    // aging <- aging[A]
    } yield new Generation(selectionStrategy, mutationStrategy, Array(elitism), new NormalConstruction, Array())

  def localOptimumParams[A](parametric: Parametric[A]): Parametric[A] =
    parametric.prefixed("Local Optimum: ")

  def defaultLocalOptimaGeneration: Parametric[Generation] = {
    for {
      selectionStrategy <- topSelection
      mutationStrategy <- hyperMutation
      elitism <- elitism
      immigrants <- randomImmigrantsElitism
    // windowing <- windowing[A]
    // aging <- aging[A]
    // niching <- niching(alg)
    } yield new Generation(selectionStrategy, mutationStrategy, Array(elitism, immigrants), new NormalConstruction, Array())
  }

  def geneticEngine(localOptimaSignal: Parametric[LocalOptimaSignal], normalGeneration: Parametric[Generation], localOptimaGeneration: Parametric[Generation]): Parametric[GeneticEngine] =
    for {
      popSize <- intParam("Population Size", default = 100, minValue = 3, maxValue = 256)
      localOptimaSignal <- localOptimaSignal
      normalGeneration <- normalGeneration
      localOptimumGeneration <- localOptimumParams(localOptimaGeneration)
    } yield new GeneticEngine(localOptimaSignal,
                              normalGeneration,
                              localOptimumGeneration,
                              popSize)

  def defaultGeneticEngine: Parametric[GeneticEngine] =
    geneticEngine(geneSimilarity, defaultNormalGeneration, defaultLocalOptimaGeneration)

  def geneticAlg[A](genetic: Parametric[Genetic[A]], engine: Parametric[GeneticEngine], rand: Random): Parametric[GeneticAlg[A]] =
    for {
      gen <- genetic
      geneticEngine <- engine
    } yield new GeneticAlg[A](gen, geneticEngine, rand)

  def defaultGeneticAlg[A](genetic: Parametric[Genetic[A]], rand: Random): Parametric[GeneticAlg[A]] =
    geneticAlg(genetic, defaultGeneticEngine, rand)


}
