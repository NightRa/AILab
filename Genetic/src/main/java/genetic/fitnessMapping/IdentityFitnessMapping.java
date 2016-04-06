package genetic.fitnessMapping;

import genetic.types.Gene;
import genetic.types.Population;

public class IdentityFitnessMapping implements FitnessMapping {
    @Override
    public <A> double mapFitness(Population<A> population, Gene<A> gene) {
        return gene.fitness;
    }
}
