package queens

import genetic.{GeneticAlg, GeneticMain}
import params.Params

object GeneticQueenMain extends GeneticMain[QueenPermutation] {
  override def intsSize(): Int = ???


  override def intsMax(): Int = ???

  override def doublesSize(): Int = ???


  override def alg(params: Params, maxTime: Double): GeneticAlg[QueenPermutation] = ???
}
