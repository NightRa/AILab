package mdKnapsack

import java.util.Random

import genetic.types.Gene
import genetic.{Genetic, Metric}
import util.{BitSet, IntBuffer}

class GeneticMDKnapsack(instance: MDKnapsackInstance, rand: Random) extends Genetic[BitSet] {
  // opt -> 0, 0 -> 1
  // ax + b:
  // b = 1
  // a * opt + 1 = 0
  // a * opt = -1
  // a = -1 / opt

  // TODO: Very fragile. Not thread-safe: should have one per thread.
  val itemsBuffer = new IntBuffer(instance.values.length)

  override def fitness(gene: BitSet): Double = {
    -instance.value(gene, itemsBuffer).toDouble / instance.optimum + 1
  }

  override def score(gene: Gene[BitSet]): Double = {
    instance.value(gene.gene, itemsBuffer)
  }

  override def randomElement(rand: Random): BitSet = {
    val items = BitSet.randomBitSet(instance.values.length, rand)
    instance.trim(items, itemsBuffer, rand)
  }

  override def mate(x: BitSet, y: BitSet): BitSet = {
    val offspring = MDKnapsack.mate(x, y, instance, rand)
    instance.trim(offspring, itemsBuffer, rand)
  }

  override def mutate(items: BitSet): BitSet = {
    val i = rand.nextInt(instance.values.length)
    items.set(i)
    instance.trim(items, itemsBuffer, rand)
  }

  override def metric(): Metric[BitSet] = new Metric[BitSet] {
    override def distance(x: BitSet, y: BitSet): Double = {
      BitSet.hammingDistance(x, y).toDouble / x.numBits
    }
  }

  override def show(gene: BitSet): String = instance.values.indices.map(i => if (gene.get(i)) '1' else '0').mkString

  override def showScientific(): Boolean = false

  override def hash(gene: BitSet): Int = gene.hashCode()
}
