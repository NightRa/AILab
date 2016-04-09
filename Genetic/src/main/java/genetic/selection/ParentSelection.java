package genetic.selection;

import genetic.types.Population;

import java.util.Random;
import java.util.function.Supplier;

public interface ParentSelection {
    <A> Supplier<A> chooseParents(Population<A> population, int size, Random rand);
}
