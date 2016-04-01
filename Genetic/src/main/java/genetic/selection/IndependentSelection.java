package genetic.selection;

import genetic.types.Population;

import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

public abstract class IndependentSelection implements SelectionStrategy {
    public abstract <A> A chooseSingleParent(Population<A> population, Random rand);

    @Override
    public final <A> Iterator<A> chooseParents(Population<A> population, int size, Random rand) {
        return new Iterator<A>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public A next() {
                return chooseSingleParent(population, rand);
            }
        };
    }

}
