package genetic.survivors;

import genetic.Genetic;
import genetic.survivors.construction.PopulationConstructor;
import genetic.types.Population;

import java.util.Random;

public class RandomImmigrants implements SurvivorSelectionStrategy {

    public final double randomImmigrantsPercent;
    public final double elitismRate;

    public RandomImmigrants(double randomImmigrantsPercent, double elitismRate) {
        this.randomImmigrantsPercent = randomImmigrantsPercent;
        this.elitismRate = elitismRate;
    }

    @Override
    public int desiredSize(int populationSize) {
        return (int) (populationSize * randomImmigrantsPercent);
    }

    @Override
    public <A> void populateSurvivors(Genetic<A> alg, Population<A> population, PopulationConstructor<A> buffer, int amount, Random rand) {
        for (int i = 0; i < amount; i++) {
            buffer.putNewGene(alg.randomElement(rand));
        }
    }
}
