package genetic.selection;

import genetic.types.Population;

import java.util.Random;

public interface SelectionStrategy {
    <A> A chooseParent(Population<A> parentsPool, Random rand);
    <A> void populateParentsPool(Population<A> population, Population<A> parentsPool, Random rand);
    <A> Population<A> initParentsPool(int populationSize);
}
