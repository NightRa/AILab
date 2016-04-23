package baldwin

import java.util
import java.util.Random

import genetic.generation.Crossover
import genetic.{Genetic, Metric}

class GeneticBaldwin(maxIterations: Int,
                     target: Array[Byte],
                     rand: Random) extends Genetic[Array[Byte]] {

  override def fitness(gene: Array[Byte]): Double = {
    if(java.util.Arrays.equals(gene, target)) 0
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

  override def hash(gene: Array[Byte]): Int = util.Arrays.hashCode(gene)
}
