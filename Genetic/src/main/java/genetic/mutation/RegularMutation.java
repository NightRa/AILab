package genetic.mutation;

import genetic.Genetic;

import java.util.Random;

public class RegularMutation extends MutationStrategy {

    public final double mutationRate;

    public RegularMutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public <A> A mutate(Genetic<A> alg, A a, Random rand) {
        if(rand.nextDouble() < mutationRate)
            return alg.mutate(a);
        else
            return a;
    }
}
