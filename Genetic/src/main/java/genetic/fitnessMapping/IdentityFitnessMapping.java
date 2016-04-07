package genetic.fitnessMapping;

import genetic.types.Gene;
import genetic.types.Population;

public class IdentityFitnessMapping<A> implements FitnessMapping<A> {
    @Override
    public double mapFitness(Population<A> population, Gene<A> gene) {
        return gene.fitness;
    }
}
