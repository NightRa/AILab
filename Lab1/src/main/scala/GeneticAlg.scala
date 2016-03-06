import java.util.Random

import Util._

import scala.annotation.tailrec

class GeneticAlg[A](alg: Genetic[A], MaxIterations: Int, show: A => String) {

  def print_best(population: Population[A], i: Int): Unit = {
    println(s"Gen $i")
    println(s"Best: ${show(population.population(0).gene)} ${population.population(0).fitness}")
    val (avg, theStdDev) = stdDev(population.population)((x: Gene[A]) => x.fitness)
    println(s"Average Fitness: $avg")
    println(s"StdDev: $theStdDev\n")
  }

  def run(population: Population[A], buffer: Population[A], i: Int = 0): Unit = {
    if (i < MaxIterations) {
      population.calc_fitness(alg)
      sortBy[Gene[A], Int](population.population)(_.fitness)
      print_best(population, i)
      if (population.population(0).fitness > 0) {
        alg.mateStrategy(population, buffer)
        run(buffer, population, i + 1)
      }
    }
  }

}

class GeneticString(heuristic: Array[Char] => Int, ElitismRate: Float, MutationRate: Float, rand: Random) extends Genetic[Array[Char]] {
  def fitness(gene: Array[Char]): Int = heuristic(gene)

  @tailrec
  final def elitism(population: Population[Array[Char]], buffer: Population[Array[Char]], elitismAmount: Int): Unit = {
    if (elitismAmount > 0) {
      buffer.population(elitismAmount - 1) = population.population(elitismAmount - 1)
      elitism(population, buffer, elitismAmount - 1)
    }
  }

  def mateStrategy(population: Population[Array[Char]], buffer: Population[Array[Char]]): Unit = {
    val popSize = population.population.length
    val elites = (popSize * ElitismRate).toInt
    elitism(population, buffer, elites)
    @tailrec
    def go(i: Int): Unit = {
      if (i < popSize) {
        val i1 = rand.nextInt(popSize / 2)
        val i2 = rand.nextInt(popSize / 2)

        buffer.population(i).gene = mate(population.population(i1).gene, population.population(i2).gene)

        if (rand.nextFloat() < MutationRate)
          buffer.population(i).gene = mutate(buffer.population(i).gene)
        go(i + 1)
      }
    }
    go(elites)
  }

  def mate(x: Array[Char], y: Array[Char]): Array[Char] = {
    val pos = rand.nextInt(x.length)
    (x.view.slice(0, pos) ++ y.view.slice(pos, y.length)).toArray
  }

  def mutate(s: Array[Char]): Array[Char] = {
    val i = rand.nextInt(s.length)

    val delta = rand.nextInt(90) + 32
    s(i) = ((s(i) + delta) % 122).toChar

    s
  }
}

object Main extends App {
  val PopulationSize: Int = 2048
  val MaxIterations: Int = 16384
  val ElitismRate: Float = 0.10f
  val MutationRate: Float = 0.99f
  val TargetString: String = "Hello world! How are you doing today? My name is Ilan. Hello world! How are you doing today? My name is Ilan. !@#^%^^$ASDasd"

  val seed = 8682522807148012L ^ System.nanoTime
  val rand = new Random(seed)

  def heuristic2: Array[Char] => Int = { s =>
    s.length - s.view.zipWithIndex.count {
      case (c, i) => c == TargetString(i)
    }
  }

  val alg = new GeneticString(heuristic2, ElitismRate, MutationRate, rand)
  val geneticAlg = new GeneticAlg(alg, MaxIterations, (arr: Array[Char]) => arr.mkString)

  val population = new Population[Array[Char]](Array.fill(PopulationSize)(new Gene(randString(TargetString.length, rand), 0)))
  val buffer = new Population[Array[Char]](Array.fill(PopulationSize)(new Gene(Array.emptyCharArray, 0)))

  geneticAlg.run(population, buffer)
}
