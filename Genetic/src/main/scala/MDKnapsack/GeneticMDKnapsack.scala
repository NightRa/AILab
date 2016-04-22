package mdKnapsack

import java.util.Random

import genetic.types.Gene
import genetic.{Genetic, Metric}
import util.BitSet

class GeneticMDKnapsack(instance: MDKnapsackInstance, rand: Random) extends Genetic[BitSet] {
  // opt -> 0, 0 -> 1
  // ax + b:
  // b = 1
  // a * opt + 1 = 0
  // a * opt = -1
  // a = -1 / opt

  override def fitness(gene: BitSet): Double = {
    -instance.value(gene).toDouble / instance.optimum + 1
  }

  override def meaningfulFitness(gene: Gene[BitSet]): Double = {
    instance.value(gene.gene)
  }

  override def randomElement(rand: Random): BitSet = {
    val items = BitSet.randomBitSet(instance.values.length, rand)
    instance.trim(items, rand)
  }

  override def mate(x: BitSet, y: BitSet): BitSet = {
    val offspring = MDKnapsack.mate(x, y, instance, rand)
    instance.trim(offspring, rand)
  }

  override def mutate(items: BitSet): BitSet = {
    val i = rand.nextInt(instance.values.length)
    items.set(i)
    instance.trim(items, rand)
  }

  override def metric(): Metric[BitSet] = new Metric[BitSet] {
    override def distance(x: BitSet, y: BitSet): Double = {
      BitSet.hammingDistance(x, y).toDouble / x.numBits
    }
  }

  override def show(gene: BitSet): String = instance.values.indices.map(i => if (gene.get(i)) '1' else '0').mkString

  override def showScientific(): Boolean = false
}
