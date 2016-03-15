package params

import java.util.Random

import genetic._
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.TopSelection
import genetic.types.{Gene, Population}

class GeneticParamsMain(main: GeneticMain[_], MaxTime: Double) extends GeneticMain[Params] {
  // Parameters
  val IntsMutationRatio: Int = 32 // 0
  val PopulationSize: Int = 20 // 1
  val Rounds: Int = 10 // 2
  val DoublesMutationSize: Double = 0.1 // 0
  val MutationRate: Double = 0.5 // 1
  val ElitismRate: Double = 0.3 // 2
  val TimeLimit: Double = 0.3 // 3
  val TopRatio: Double = 0.5 // 4
  // Derived values
  val intsMutationSize: Int = main.intsMax() / IntsMutationRatio

  def intsSize(): Int = 3

  def intsMax(): Int = 50

  def doublesSize(): Int = 5

  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  def mateStrategy(params: Params) = {
    val ElitismRate = params.doubles(2)
    val MutationRate = params.doubles(1)
    new ElitismMutationMateStrategy(ElitismRate, MutationRate, rand)
  }

  def selectionStrategy(params: Params) = new TopSelection(params.doubles(4))

  def genetic(params: Params) = new GeneticParams(main, params.ints(0), params.doubles(0), params.doubles(1), params.doubles(3), params.ints(2), rand)

  def randomParams(): Params = new Params(
    Array.fill(main.intsSize)(rand.nextInt(main.intsMax())),
    main.intsMax(),
    Array.fill(main.doublesSize())(rand.nextDouble()))

  val emptyParams: Params = new Params(Array.emptyIntArray, main.intsMax(), Array.emptyDoubleArray)
  val emptyParamsGene: Gene[Params] = new Gene(emptyParams, 0)

  def initPopulation(params: Params) = new Population[Params](Array.fill(params.ints(1))(new Gene(randomParams(), 0)))

  def initBuffer(params: Params) = new Population[Params](Array.fill(params.ints(1))(emptyParamsGene))

  def alg(params: Params, maxTime: Double): GeneticAlg[Params] =
    new GeneticAlg[Params](genetic(params), mateStrategy(params), selectionStrategy(params), maxTime, rand, initPopulation(params), initBuffer(params), _.toString)

  def main(args: Array[String]) {

    alg(new Params(Array(intsMutationSize, PopulationSize, Rounds), intsMax(), Array(DoublesMutationSize, MutationRate, ElitismRate, TimeLimit, TopRatio)), MaxTime).run(print = true)

  }
}
