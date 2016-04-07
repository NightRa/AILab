package genetic.localOptima

import genetic.types.Population
import util.{RunningStat, Util}

case class StdDevLocalOptimaDetector[A](stdDevThreshold : Double) extends LocalOptimaSignal[A]{

  override def isInLocalOptima(population: Population[A]): Boolean = {
    val statistics = new RunningStat()
    var i = 0
    while (i < population.population.length) {
      statistics.push(population.population(i).fitness)
      i += 1
    }
    val stdDev = statistics.standardDeviation()
    stdDev < stdDevThreshold
  }
}
