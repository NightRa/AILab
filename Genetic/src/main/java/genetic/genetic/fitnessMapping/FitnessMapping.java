package genetic.genetic.fitnessMapping;

import genetic.genetic.Metric;
import genetic.genetic.types.Gene;
import genetic.genetic.types.Population;

public interface FitnessMapping {
    <A> double mapFitness(Metric<A> metric, Population<A> population, Gene<A> gene);
}
