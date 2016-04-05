package baldwin

import genetic.{Metric, Genetic}

import scala.util.Random

class GeneticBaldwin(mateFunc: (Array[Bit], Array[Bit], Random) => Array[Bit],
                     mutateFunc: (Array[Bit], Random) => Unit,
                     rand: Random, baldwinSearchIterations : Int,
                     bitsToSearch : Array[Bit]) extends Genetic[Array[Bit]] {
  override def fitness(gene: Array[Bit]): Double = {
    BaldwinUtils.dist(gene, bitsToSearch)
  }

  override def mate(x: Array[Bit], y: Array[Bit]): Array[Bit] = mateFunc (x, y, rand)

  override def metric(): Metric[Array[Bit]] = new Metric[Array[Bit]] {
    override def distance(x: Array[Bit], y: Array[Bit]): Double = BaldwinUtils.dist(x, y)
  }

  override def mutate(a: Array[Bit]): Array[Bit] = {
    mutateFunc(a, rand)
    a
  }
}

object BaldwinUtils {
  def dist (x: Array[Bit], y: Array[Bit]): Double = {
    var notSames = 0
    for (i <- x.indices){
      (x(i), y(i)) match {
        case (One(), One())   => ()
        case (Zero(), Zero()) => ()
        case _                => notSames += 1
      }
    }
    notSames.toDouble /  x.length
  }
}
