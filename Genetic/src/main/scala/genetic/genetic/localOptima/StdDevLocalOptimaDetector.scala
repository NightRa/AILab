package genetic.genetic.localOptima

import genetic.genetic.Metric
import genetic.genetic.types.Population
import genetic.util.RunningStat

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
