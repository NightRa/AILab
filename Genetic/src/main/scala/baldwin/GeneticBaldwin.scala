package baldwin

import java.io.{FileOutputStream, PrintStream}
import java.nio.file.{Files, Paths}
import java.util.Arrays
import java.util.Random

import genetic.generation.Crossover
import genetic.types.Population
import genetic.{Genetic, Metric}

class GeneticBaldwin(maxIterations: Int,
                     target: Array[Byte],
                     rand: Random) extends Genetic[Array[Byte]] {

  //noinspection ReferenceMustBePrefixed
  override def fitness(gene: Array[Byte]): Double = {
    if(Arrays.equals(gene, target)) 0
    else {
      // do not let the fitness to be 0
      val remainingIterations = Math.min(BaldwinBitString.localSearchesTimeRemaining(gene, target, maxIterations, rand), maxIterations - 1)
      val rawFitness = 1 + 19.0 * remainingIterations.toDouble / maxIterations // 1~20
      val normalized = 1 - rawFitness / 20 // 1 -> 1, 20 -> 0
      normalized
    }
  }

  override def mate(x: Array[Byte], y: Array[Byte]): Array[Byte] = Crossover.onePointCrossoverBytes(x,y, rand)

  override def randomElement(rand: Random): Array[Byte] = BaldwinBitString.randomBaldwinString(target.length ,rand)

  override def metric(): Metric[Array[Byte]] = new Metric[Array[Byte]] {
    override def distance(x: Array[Byte], y: Array[Byte]): Double = 1
  }

  override def show(gene: Array[Byte]): String = {
    gene.iterator.map {
      case BaldwinBit.Zero => '0'
      case BaldwinBit.One => '1'
      case BaldwinBit.QuestionMark => '?'
    }.mkString
  }

  override def showScientific: Boolean = false

  override def mutate(a: Array[Byte]): Array[Byte] = {
    val index = rand.nextInt(a.length)
    a(index) = BaldwinBit.genBaldwinBit(rand)
    a
  }

  Files.createDirectories(Paths.get("baldwin/"))
  val correct = new PrintStream(new FileOutputStream(s"baldwin/correct.csv"))
  val incorrect = new PrintStream(new FileOutputStream(s"baldwin/incorrect.csv"))
  val percent = new PrintStream(new FileOutputStream(s"baldwin/percent.csv"))

  override def postGenerationEffect(generation: Int, before: Population[Array[Byte]], after: Population[Array[Byte]]): Unit = {
    def correctBits(baldwinString: Array[Byte]): Int = {
      var i = 0
      var correct = 0
      while(i < baldwinString.length) {
        if(baldwinString(i) == target(i)) correct += 1
        i += 1
      }
      correct
    }
    def incorrectBits(baldwinString: Array[Byte]): Int = {
      var i = 0
      var incorrect = 0
      while(i < baldwinString.length) {
        if(baldwinString(i) != target(i) && baldwinString(i) != BaldwinBit.QuestionMark) incorrect += 1
        i += 1
      }
      incorrect
    }

    val correctBefore = before.population.map(x => correctBits(x.gene)).sum.toDouble
    val correctAfter = after.population.map(x => correctBits(x.gene)).sum.toDouble
    correct.append(s"$generation,${correctAfter / after.population.length}\n")
    incorrect.append(s"$generation,${after.population.map(x => incorrectBits(x.gene)).sum.toDouble / after.population.length}\n")
    percent.append(s"$generation,${correctAfter / correctBefore - 1}\n")
  }

  override def endAlgEffect(): Unit = {
    correct.close()
    incorrect.close()
    percent.close()
  }
}
