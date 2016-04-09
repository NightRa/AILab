package genetic.fitnessMapping;

import genetic.Metric;
import genetic.types.Gene;
import genetic.types.Population;

public interface FitnessMapping {
    <A> double mapFitness(Metric<A> metric, Population<A> population, Gene<A> gene);
}
