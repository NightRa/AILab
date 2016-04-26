package genetic.genetic.fitnessMapping;

import genetic.genetic.Metric;
import genetic.genetic.types.Gene;
import genetic.genetic.types.Population;

public class SigmaScaling implements FitnessMapping {
    private static final double epsilon = 1e-10;

    @Override
    public <A> double mapFitness(Metric<A> metric, Population<A> population, Gene<A> gene) {
        if(gene.fitness == 0.0) return 0;
        if(population.fitnessStdDev() == 0) return 0.99;
        // 1/g such that it's a minimization problem.
        return Math.min(0.99, Math.max(epsilon,  1 / (Math.abs(gene.fitness - population.fitnessAvg()) / 2 * population.fitnessStdDev())));
    }
}
