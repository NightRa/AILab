package genetic.localOptima

import genetic.types.Population
import util.Util

case class StdDevLocalOptimaDetector[A](stdDevThreshold : Double) extends LocalOptimaSignal[A]{

  override def isInLocalOptima(population: Population[A]): Boolean = {
    val fitnesses = population.population.map(_.fitness)
    val (avg, stdDev) = Util.stdDev(fitnesses) (identity)
    stdDev < stdDevThreshold
  }
}
