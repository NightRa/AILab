package genetic.localOptima

import java.util.DoubleSummaryStatistics

import genetic.Metric
import genetic.types.Population
import util.{RunningStat, Util}

class DistanceSimilarityDetector[A](distanceThresh : Double, metric: Metric[A]) extends LocalOptimaSignal[A]{

  override def isInLocalOptima(population: Population[A]): Boolean = {
    val statistics = new RunningStat()
    var i = 0
    while (i < population.population.length) {
      statistics.push(metric.distance(population.population(0).gene, population.population(i).gene))
      i += 1
    }

    val distanceStdDev = statistics.standardDeviation()
    distanceStdDev < distanceThresh
  }
}
