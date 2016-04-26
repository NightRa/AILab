package genetic.genetic.selection;

import genetic.genetic.types.Population;

import java.util.Random;

public class Ranking extends IndependentSelection {

    @Override
    public <A> A chooseSingleParent(Population<A> population, Random rand) {
        int popSize = population.population.length;
        double maxFitness = popSize - 1;
        int index;
        do {
            index = rand.nextInt(popSize); // 1/N probability to choose anyone
        } while (rand.nextDouble() >= index / maxFitness);
        return population.population[index].gene;
    }
}
