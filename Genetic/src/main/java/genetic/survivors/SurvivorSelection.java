package genetic.survivors;

import genetic.Genetic;
import genetic.types.Population;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public interface SurvivorSelection<A> {
    void selectSurvivors(Genetic<A> alg, Population<A> population, Population<A> buffer, Function<Integer, Supplier<A>> getChildren, Random rand);
}
