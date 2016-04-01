package genetic.selection;

import genetic.types.Population;

import java.util.Random;

public interface SelectionStrategy {
    <A> A chooseParent(Population<A> population, Random rand);
}
