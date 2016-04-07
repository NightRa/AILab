package parametric


import genetic.Genetic
import genetic.fitnessMapping.{FitnessMapping, Niching}
import Parametric._
import genetic.generation.Generation
import genetic.localOptima.{DistanceSimilarityDetector, StdDevLocalOptimaDetector}
import genetic.mutation.RegularMutation
import genetic.selection.TopSelection
import genetic.survivors.{Elitism, ElitismRandomImmigrants}

object Instances {
  def niching[A](alg: Genetic[A]): Parametric[Niching[A]] =
    for {
      alpha <- doubleParam("Niching alpha", 1)
      sigmaShare <- doubleParam("Niching sigma share", 0.01)
    } yield new Niching[A](alpha, sigmaShare, alg.metric())

  def geneSimilarity[A](alg: Genetic[A]): Parametric[DistanceSimilarityDetector[A]] =
    for {
      distanceThresh <- doubleParam("Gene Similarity Threshold", 0.05)
    } yield new DistanceSimilarityDetector[A](distanceThresh, alg.metric())

  def stdDevSimilarity[A](alg: Genetic[A]): Parametric[StdDevLocalOptimaDetector[A]] =
    for {
      stdDevThreshold <- doubleParam("Std. Dev. Similarity Threshold", 0.01)
    } yield new StdDevLocalOptimaDetector[A](stdDevThreshold)

  def elitism[A]: Parametric[Elitism[A]] =
    for {
      elitismRate <- doubleParam("Elitism Rate", 0.2)
    } yield new Elitism[A](elitismRate)

  def randomImmigrantsElitism[A]: Parametric[ElitismRandomImmigrants[A]] =
    for {
      elitismRate <- doubleParam("Elitism Rate", 0.1)
      randomImmigrantsRate <- doubleParam("Immigrants Rate", 0.1)
    } yield new ElitismRandomImmigrants[A](randomImmigrantsRate, elitismRate)

  def topSelection: Parametric[TopSelection] =
    for {
      topRatio <- doubleParam("Top Ratio", 0.8)
    } yield new TopSelection(topRatio)

  def mutation: Parametric[RegularMutation] =
    for {
      mutationRate <- doubleParam("Mutation Rate", 0.3)
    } yield new RegularMutation(mutationRate)

  def hyperMutation: Parametric[RegularMutation] =
    for {
      mutationRate <- doubleParam("Hyper Mutation Rate", 0.5)
    } yield new RegularMutation(mutationRate)

  def normalGeneration[A]: Parametric[Generation[A]] =
    for {
      selectionStrategy <- topSelection
      mutationStrategy <- mutation
      survivorSelection <- elitism[A]
    } yield new Generation[A](selectionStrategy, mutationStrategy, survivorSelection, Array.empty)

  def localOptimaGeneration[A](alg: Genetic[A]): Parametric[Generation[A]] = {
    val prefix = "Local Optimum: "
    (for {
      selectionStrategy <- topSelection
      mutationStrategy <- hyperMutation
      survivorSelection <- randomImmigrantsElitism[A]
      // TODO: return niching
      // niching <- niching(alg)
    } yield new Generation[A](selectionStrategy, mutationStrategy, survivorSelection, Array(/*niching*/))).prefixed(prefix)
  }

}
