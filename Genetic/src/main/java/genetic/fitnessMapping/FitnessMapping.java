package genetic.fitnessMapping;

import genetic.types.Gene;
import genetic.types.Population;

public interface FitnessMapping {
    <A> double mapFitness(Population<A> population, Gene<A> gene);
}
