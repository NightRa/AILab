package genetic.selection;

import genetic.types.Population;

import java.util.Random;
import java.util.function.Supplier;

public abstract class IndependentSelection implements ParentSelection {
    public abstract <A> A chooseSingleParent(Population<A> population, Random rand);

    @Override
    public final <A> Supplier<A> chooseParents(Population<A> population, int size, Random rand) {
        return () -> chooseSingleParent(population, rand);
    }

}
