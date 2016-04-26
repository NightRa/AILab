package genetic.genetic.survivors;

import genetic.genetic.Genetic;
import genetic.genetic.survivors.construction.PopulationConstructor;
import genetic.genetic.types.Population;

import java.util.Random;

public interface SurvivorSelectionStrategy {
    int desiredSize(int populationSize);
    <A> void populateSurvivors(Genetic<A> alg, Population<A> population, PopulationConstructor<A> buffer, int amount, Random rand);
}
