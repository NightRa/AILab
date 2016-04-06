package baldwin

import genetic.{Metric, Genetic}

import java.util.Random

class GeneticBaldwin(maxIterations: Int,
                     target: Array[Bit],
                     rand: Random) extends Genetic[Array[BaldwinBit]] {
  override def fitness(gene: Array[BaldwinBit]): Double = {
    val remainingIterations = BaldwinBitString.localSearchToBest(gene, maxIterations, BaldwinBitString.bitStringEquals(_, target), rand)
    val rawFitness = 1 + 19.0 * remainingIterations.toDouble / maxIterations // 1~20
    val normalized = (20 -  rawFitness) / 20 // 1 -> 1, 20 -> 0
    assert(normalized >= 0 && normalized <= 1)
    normalized
  }

  override def metric(): Metric[Array[BaldwinBit]] = new Metric[Array[BaldwinBit]] {
    override def distance(x: Array[BaldwinBit], y: Array[BaldwinBit]): Double = 1
  }

  override def mate(x: Array[BaldwinBit], y: Array[BaldwinBit]): Array[BaldwinBit] = {
    BaldwinBitString.onePointCrossOver(x, y, rand)
  }

  override def mutate(a: Array[BaldwinBit]): Array[BaldwinBit] = {
    val index = rand.nextInt(a.length)
    a(index) = BaldwinBitString.genBaldwinBit(rand)
    a
  }

  override def randomElement(rand: Random): Array[BaldwinBit] = BaldwinBitString.generateBitStringRandomly(target.length,rand)
  override def show(gene: Array[BaldwinBit]): String = gene.mkString
}
