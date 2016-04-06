package genetic.survivors;

import genetic.types.Population;

import java.util.function.Function;
import java.util.function.Supplier;

public interface SurvivorSelection<A> {
    void selectSurvivors(Population<A> population, Population<A> buffer, Function<Integer, Supplier<A>> getChildren);
}
