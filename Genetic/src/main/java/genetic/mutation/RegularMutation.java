package genetic.mutation;

import genetic.Genetic;

import java.util.Random;

public class RegularMutation extends MutationStrategy {

    public final double mutationRate;
    public final Random rand;

    public RegularMutation(double mutationRate, Random rand) {
        this.mutationRate = mutationRate;
        this.rand = rand;
    }

    @Override
    public <A> A mutate(Genetic<A> alg, A a) {
        if(rand.nextDouble() < mutationRate)
            return alg.mutate(a);
        else
            return a;
    }
}
