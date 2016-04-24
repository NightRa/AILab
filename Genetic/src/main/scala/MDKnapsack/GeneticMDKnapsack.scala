package mdKnapsack

import java.util.Random

import genetic.types.Gene
import genetic.{Genetic, Metric}
import util.{BitSet, IntBuffer}

class GeneticMDKnapsack(instance: MDKnapsackInstance, rand: Random) extends Genetic[BitSet] {

  // TODO: Very fragile. Not thread-safe: should have one per thread.
  // Current implementation requires a new GeneticMDKnapsack to be allocated for each thread
  // to fulfill the proof of ownership for the IntBuffer.
  // That means it requires global reasoning to supply correctness. Not so good.
  // Alternatives:
  // 1. Allocate inside each function => Large overhead
  // 2. Thread local storage, have a single buffer of the 'current' population size.
  // 3. Shared Resource allocator - which allocates the buffers for temporary use and reuses them.
  // For the time being I'll use the fragile, global reasoning solution, because the others seem like a lot of work.
  val itemsBuffer =
    new IntBuffer(instance.values.length)

  // @Requires a proof of ownership for the IntBuffer: Cannot be fulfilled, Abstraction boundary.
  override def fitness(gene: BitSet): Double = {
    1 - instance.value(gene, itemsBuffer).toDouble / instance.optimum
  }

  // @Requires a proof of ownership for the IntBuffer: Cannot be fulfilled, Abstraction boundary.
  override def score(gene: Gene[BitSet]): Double = {
    instance.value(gene.gene, itemsBuffer)
  }

  // @Requires a proof of ownership for the IntBuffer: Cannot be fulfilled, Abstraction boundary.
  override def randomElement(rand: Random): BitSet = {
    val items = BitSet.randomBitSet(instance.values.length, rand)
    instance.trim(items, itemsBuffer, rand)
  }

  // @Requires a proof of ownership for the IntBuffer: Cannot be fulfilled, Abstraction boundary.
  override def mate(x: BitSet, y: BitSet): BitSet = {
    val offspring = MDKnapsack.mate(x, y, instance, rand)
    instance.trim(offspring, itemsBuffer, rand)
  }

  // @Requires a proof of ownership for the IntBuffer: Cannot be fulfilled, Abstraction boundary.
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
