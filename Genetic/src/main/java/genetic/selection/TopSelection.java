package genetic.selection;

import genetic.types.Population;

import java.util.Random;

public class TopSelection extends IndependentSelection {
    public final double topRatio;

    public TopSelection(double topRatio) {
        this.topRatio = topRatio;
    }

    @Override
    public <A> A chooseSingleParent(Population<A> population, Random rand) {
        int parentsPool = Math.max((int) (population.population.length * topRatio), 1);
        // TODO: Fails here with non-positive parentsPool size
        return population.population[rand.nextInt(parentsPool)].gene;
    }
}
