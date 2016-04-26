package genetic.genetic.selection;

import genetic.genetic.types.Population;

import java.util.Iterator;
import java.util.Random;

public interface SelectionStrategy {
    <A> Iterator<A> chooseParents(Population<A> population, int size, Random rand);
}
