package genetic.selection;

import genetic.types.Gene;
import genetic.types.Population;

import java.util.Random;

public class TopSelection implements SelectionStrategy {
    public final double topRatio;

    public TopSelection(double topRatio) {
        this.topRatio = topRatio;
    }

    @Override
    public <A> A chooseParent(Population<A> population, Random rand) {
        int parentsPool = (int) (population.population.length * topRatio);
        return population.population[rand.nextInt(parentsPool)].gene;
    }
}
