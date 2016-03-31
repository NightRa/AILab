package genetic.mating;

import genetic.Genetic;
import genetic.types.Population;
import genetic.selection.SelectionStrategy;

public interface MateStrategy {
    <A> void mateStrategy(Genetic<A> alg,
                          SelectionStrategy selection,
                          Population<A> population,
                          Population<A> buffer,
                          Population<A> parentsPool,
                          boolean isInLocalOptimum);
}
