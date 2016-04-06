package genetic.mutation;

import genetic.Genetic;

public abstract class MutationStrategy {
    public abstract <A> A mutate(Genetic<A> alg, A a);
}
