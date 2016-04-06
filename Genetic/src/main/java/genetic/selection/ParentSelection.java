package genetic.selection;

import genetic.fitnessMapping.FitnessMapping;
import genetic.types.Population;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Supplier;

public interface ParentSelection {
    <A> Supplier<A> chooseParents(Population<A> population, int size, Random rand);
}
