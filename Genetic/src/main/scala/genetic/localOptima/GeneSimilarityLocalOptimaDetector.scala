package genetic.localOptima

import genetic.Metric
import genetic.types.Population
import util.Util

class GeneSimilarityLocalOptimaDetector[A](distanceThresh : Double ,metric: Metric[A]) extends LocalOptimaSignal[A]{
  override def isInLocalOptima(population: Population[A]): Boolean = {

    val distances = for {
      g1 <- population.population
    } yield metric.distance(population.population(0).gene, g1.gene)
    val distanceStdDev = Util.stdDev(distances)(identity)._2
    distanceStdDev < distanceThresh
  }
}
