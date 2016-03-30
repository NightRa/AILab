package genetic.selection;

import genetic.types.Gene;
import genetic.types.Population;
import util.JavaUtil;

import java.util.Random;

public class Tournament implements SelectionStrategy {
    public final int tournamentSize;
    public final double chooseBestProbability;

    public Tournament(int tournamentSize, double chooseBestProbability) {
        this.tournamentSize = tournamentSize;
        this.chooseBestProbability = chooseBestProbability;
    }

    public <A> A chooseParent(Population<A> parentsPool, Random rand) {
        int i = 0;
        while(true) {
            if(i == tournamentSize) i = 0;
            else if(rand.nextFloat() < chooseBestProbability) return parentsPool.population[i].gene;
            else i++;
        }
    }

    @Override
    public <A> void populateParentsPool(Population<A> population, Population<A> parentsPool, Random rand) {
        assert parentsPool.population.length == tournamentSize;
        int popSize = population.population.length;
        for (int i = 0; i < parentsPool.population.length; i++) {
            parentsPool.population[i] = population.population[rand.nextInt(popSize)];
        }
        JavaUtil.sortGenes(parentsPool.population);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> Population<A> initParentsPool(int populationSize) {
        int poolSize = this.tournamentSize;
        return new Population<A>(new Gene[poolSize]);
    }


}
