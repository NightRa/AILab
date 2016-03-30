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
    public <A> A chooseParent(Population<A> parentsPool, Random rand) {
        int poolSize = parentsPool.population.length;
        int i = rand.nextInt(poolSize);
        return parentsPool.population[i].gene;
    }

    @Override
    public <A> void populateParentsPool(Population<A> population, Population<A> parentsPool, Random rand) {
        System.arraycopy(population.population, 0, parentsPool.population, 0, parentsPool.population.length);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Population<A> initParentsPool(int popSize) {
        int poolSize = Math.max(Math.min((int) (popSize * topRatio), popSize), 1);
        return new Population<>(new Gene[poolSize]);
    }
}
