package baldwin

import java.io.{IOException, FileWriter}
import java.util.Comparator._
import java.util.stream.{Collectors, Stream}

import genetic.localOptima.{LocalOptimaSignal, IgnoreLocalOptima}
import genetic.mating.ElitismMutationMateStrategy
import genetic.selection.{RouletteWheelSelection, TopSelection}
import genetic.types.{Gene, Population}
import genetic.{Genetic, GeneticAlg, GeneticMain}
import params.{GeneticParamsMain, NamedParams, Params}
import string.StringHeuristics
import java.util.Random

import func.FuncSolution
import genetic.fitnessMapping.FitnessMapping
import genetic.generation.Generation
import genetic.mutation.RegularMutation
import genetic.survivors.Elitism
import util.Util._

class BaldwinMain(targetBits: Array[Bit]) extends GeneticMain[Array[BaldwinBit]] {
  val name: String = "Baldwin's bit-string search"
  val MaxTime: Double = 20.0
  val printEvery = 1
  override val intsMax: Int = 1024
  override val defaultParams = NamedParams(
    "Population Size" -> 100,
    "Max Iterations" -> 1000
  )(
    "Elitism Rate" -> 0,
    "Mutation Rate" -> 0.05
  )

  def numOfCorrectPositions(a: Array[BaldwinBit]): Int = {
    var sum = 0
    for (i <- a.indices) {
      a(i) match {
        case DataBit(bit) =>
          if (bit.equals(targetBits(i)))
            sum += 1
        case _ => ()
      }
    }
    sum
  }

  def numOfInCorrectPositions(a: Array[BaldwinBit]): Int = {
    var sum = 0
    for (i <- a.indices) {
      a(i) match {
        case DataBit(bit) =>
          if (!bit.equals(targetBits(i)))
            sum += 1
        case _ => ()
      }
    }
    sum
  }

  def alg(params: Params): MyGeneticAlg = {
    val PopulationSize = params.ints(0)
    val maxIterations = params.ints(1)
    val ElitismRate = params.doubles(0)
    val MutationRate = params.doubles(1)

    val genetic = new GeneticBaldwin(maxIterations, targetBits, rand)

    val selectionStrategy = new RouletteWheelSelection()
    val localOptimaDetector = new IgnoreLocalOptima[Array[BaldwinBit]]()
    val survivorSelection = new Elitism[Array[BaldwinBit]](ElitismRate)
    val mutationStrategy = new RegularMutation(MutationRate, rand)
    val normalGeneration = new Generation[Array[BaldwinBit]](
      selectionStrategy,
      mutationStrategy,
      survivorSelection,
      Array.empty,
      rand
    )

    new MyGeneticAlg(
      numOfCorrectPositions,
      numOfInCorrectPositions,
      genetic,
      localOptimaDetector,
      normalGeneration,
      normalGeneration,
      PopulationSize,
      rand
    )
  }

}

class MyGeneticAlg(numOfCorrectPositions: Array[BaldwinBit] => Int, numOfInCorrectPositions: Array[BaldwinBit] => Int, genetic: Genetic[Array[BaldwinBit]],
                   localOptimaSignal: LocalOptimaSignal[Array[BaldwinBit]],
                   normalGeneration: Generation[Array[BaldwinBit]],
                   localOptimaGeneration: Generation[Array[BaldwinBit]],
                   PopulationSize: Int,
                   rand: Random) extends GeneticAlg[Array[BaldwinBit]](genetic, localOptimaSignal, normalGeneration, localOptimaGeneration, PopulationSize, rand) {
  var lastLastLearned : Double = 0;
  var lastLearned: Option[Double] = None
  var sumWindow : Double = 0
  val correctCsv = new FileWriter("Correct.csv")
  val incorrectCsv = new FileWriter("Incorrect.csv")
  val percentCsv = new FileWriter("Percent.csv")

  def addToFile(fileWriter: FileWriter, i: Int, num: Double): Unit = {
    fileWriter.write(i + "," + num + "\n") //appends the string to the file
  }
  var corrects : Double = 0
  var incorrects : Double = 0
  override def print_best(population: Population[Array[BaldwinBit]], i: Int): Unit = {
    val (avg, theStdDev) = stdDev(population.population)((x: Gene[Array[BaldwinBit]]) => x.fitness)
    println(f"Best ($i): ${genetic.show(population.population(0).gene)}; fitness: ${formatScientific(population.population(0).fitness, 3)}; avg: $avg%.3f; stdDev: $theStdDev%.3f")
    corrects = population.population.iterator
      .map(_.gene)
      .map(x => numOfCorrectPositions(x) / x.length.toDouble)
      .sum / population.population.length.toDouble
    println(f"($i): Average correct: " + corrects)
    addToFile(correctCsv, i, corrects)
    incorrects = population.population.iterator
      .map(_.gene)
      .map(x => numOfInCorrectPositions(x) / x.length.toDouble)
      .sum / population.population.length.toDouble
    println(f"($i): Average incorrect: " + incorrects)
    addToFile(incorrectCsv, i, incorrects)
    val length = population.population(0).gene.length.toDouble
    val learned = (corrects + incorrects) /// length
    lastLearned match {
      case None => ()
      case Some(lastLearned) =>
        lastLastLearned = lastLearned
        val percent = (learned - lastLearned) /// length
        sumWindow+= percent
        if (i % 1 == 0) {
          addToFile(percentCsv, i, sumWindow)
          sumWindow = 0
        }

        println(f"($i): percent learned: " + percent + "%")
        //addToFile(percentCsv, i, percent)
    }
    lastLearned = Some(learned)
  }


}

object StartingBits {
  val target = Array.fill[Bit](20)(Zero)
}

object GeneticBaldwinMain extends BaldwinMain(StartingBits.target)