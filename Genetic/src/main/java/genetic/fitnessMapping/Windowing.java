package genetic.fitnessMapping;

import genetic.Metric;
import genetic.types.Gene;
import genetic.types.Population;

public class Windowing implements FitnessMapping {
    private static final double epsilon = 1e-10;

    @Override
    public <A> double mapFitness(Metric<A> metric, Population<A> population, Gene<A> gene) {
        // return 1 - ((1 - gene.fitness) - (1 - population.worstMaxFitness()));
        if(gene.fitness == 0)
            return 0;
        else {
            double fitness = 1 + gene.fitness - population.worstMaxFitness();
            return Math.max(fitness, epsilon);
        }
    }
}
