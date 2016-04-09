package genetic.types;

import genetic.Genetic;
import genetic.fitnessMapping.FitnessMapping;

public class Population<A> {
    public Gene<A>[] population;
    private double minFitness;
    private double maxFitness;

    public Population(Gene<A>[] population) {
        this.population = population;
    }

    public void calc_fitness(Genetic<A> alg, FitnessMapping[] mappings) {
        minFitness = Double.POSITIVE_INFINITY;
        maxFitness = Double.NEGATIVE_INFINITY;
        for (Gene<A> gene : population) {
            gene.fitness = alg.fitness(gene.gene);
            gene.age += 1;
            if (gene.fitness < minFitness) minFitness = gene.fitness;
            if (gene.fitness > maxFitness) maxFitness = gene.fitness;
        }
        for (Gene<A> gene : population) {
            for (FitnessMapping mapping : mappings) {
                gene.fitness = mapping.mapFitness(alg.metric(), this, gene);
            }
        }
    }

    // Was very performance critical
    public double bestMinFitness() {
        return minFitness;
    }

    public double worstMaxFitness() {
        return maxFitness;
    }

}
