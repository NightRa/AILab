package genetic.genetic.fitnessMapping;

import genetic.genetic.Metric;
import genetic.genetic.types.Gene;
import genetic.genetic.types.Population;

public class ExponentialScaling implements FitnessMapping {
    @Override
    public <A> double mapFitness(Metric<A> metric, Population<A> population, Gene<A> gene) {
        return 1 - Math.sqrt(1 - gene.fitness);
    }
}
