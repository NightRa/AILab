package genetic.localOptima

import genetic.Metric
import genetic.types.Population
import util.RunningStat

case class StdDevLocalOptimaDetector(stdDevThreshold: Double) extends LocalOptimaSignal {
  override def isInLocalOptima[A](metric: Metric[A], population: Population[A]): Boolean = {
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
