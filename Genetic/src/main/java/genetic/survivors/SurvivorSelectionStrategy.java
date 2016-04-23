package genetic.survivors;

import genetic.Genetic;
import genetic.survivors.construction.PopulationConstructor;
import genetic.types.Population;

import java.util.Random;

public interface SurvivorSelectionStrategy {
    int desiredSize(int populationSize);
    <A> void populateSurvivors(Genetic<A> alg, Population<A> population, PopulationConstructor<A> buffer, int amount, Random rand);
}
