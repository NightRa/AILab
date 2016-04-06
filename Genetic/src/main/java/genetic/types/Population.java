package genetic.types;

import genetic.Genetic;
import genetic.fitnessMapping.FitnessMapping;

import java.util.List;

public class Population<A> {
    public Gene<A>[] population;

    public Population(Gene<A>[] population) {
        this.population = population;
    }

    public void calc_fitness(Genetic<A> alg, FitnessMapping[] mappings) {
        for(Gene<A> gene: population) {
            gene.fitness = alg.fitness(gene.gene);
            for(FitnessMapping mapping: mappings) {
                gene.fitness = mapping.mapFitness(this, gene);
            }
        }
    }

}
