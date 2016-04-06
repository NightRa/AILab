package params

import genetic._
import genetic.fitnessMapping.FitnessMapping
import genetic.generation.Generation
import genetic.localOptima.IgnoreLocalOptima
import genetic.mutation.RegularMutation
import genetic.selection.TopSelection
import genetic.survivors.Elitism
import util.JavaUtil

class GeneticParamsMain(main: GeneticMain[_], override val MaxTime: Double) extends GeneticMain[Params] {
  override def printEvery = 1
  val name = "Genetic Parameter Search"
  override val intsMax: Int = 48
  override val defaultParams = NamedParams(
    "Ints Mutation Size" -> 32,
    "Population Size" -> 20,
    "Rounds" -> 10
  )(
    "Doubles Mutation Size" -> 0.1,
    "Mutation Rate" -> 0.5,
    "Elitism Rate" -> 0.16,
    "Time Limit (Seconds)" -> 0.3,
    "Top Ratio" -> 0.8
  )

  def printer(timeSec: Double, timeFraction: Double, params: Params) = {
    val timeMs = JavaUtil.formatDouble(timeSec * 1000, 3)
    s"$timeMs ms; " + (if (timeFraction > 0.99) params else "")
  }

  def alg(params: Params): GeneticAlg[Params] = {
    val IntsMutationSize = params.ints(0)
    val PopulationSize = params.ints(1)
    val Rounds = params.ints(2)
    val DoublesMutationSize = params.doubles(0)
    val MutationRate = params.doubles(1)
    val ElitismRate = params.doubles(2)
    val TimeLimit = params.doubles(3)
    val TopRatio = params.doubles(4)

    val genetic = new GeneticParams(main, IntsMutationSize, DoublesMutationSize, MutationRate, TimeLimit, Rounds, printer, rand)

    val selectionStrategy = new TopSelection(TopRatio)
    val localOptimaDetector = new IgnoreLocalOptima[Params]()
    val survivorSelection = new Elitism[Params](ElitismRate)
    val mutationStrategy = new RegularMutation(MutationRate, rand)
    val normalGeneration = new Generation[Params](
      selectionStrategy,
      mutationStrategy,
      survivorSelection,
      Array.empty[FitnessMapping],
      rand
    )

    new GeneticAlg[Params](
      genetic,
      localOptimaDetector,
      normalGeneration,
      normalGeneration,
      PopulationSize,
      rand
    )
  }
}

