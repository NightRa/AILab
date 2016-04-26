package genetic.genetic.survivors;

import genetic.genetic.Genetic;
import genetic.genetic.survivors.construction.PopulationConstructor;
import genetic.genetic.types.Population;

import java.util.Random;

public class Elitism implements SurvivorSelectionStrategy {
    public final double elitismRate;

    public Elitism(double elitismRate) {
        this.elitismRate = elitismRate;
    }

    @Override
    public int desiredSize(int populationSize) {
        return (int) (populationSize * elitismRate);
    }

    @Override
    public <A> void populateSurvivors(Genetic<A> alg, Population<A> population, PopulationConstructor<A> buffer, int amount, Random rand) {
        for (int i = 0; i < amount; i++) {
            buffer.putOldGene(population.population[i].gene);
        }
    }
}
