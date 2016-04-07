package genetic.mutation;

import genetic.Genetic;

import java.util.Random;

public abstract class MutationStrategy {
    public abstract <A> A mutate(Genetic<A> alg, A a, Random rand);
}
