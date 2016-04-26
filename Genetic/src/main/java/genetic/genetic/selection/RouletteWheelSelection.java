package genetic.genetic.selection;

import genetic.genetic.types.Population;

import java.util.Random;

// Implemented using Stochastic Acceptance in O(1):
//    http://arxiv.org/abs/1109.3627
public class RouletteWheelSelection extends IndependentSelection {

    @Override
    public <A> A chooseSingleParent(Population<A> population, Random rand) {
        int popSize = population.population.length;
        double maxFitness = 1 - population.bestMinFitness();
        int index;
        do {
            index = rand.nextInt(popSize); // 1/N probability to choose anyone
        } while (rand.nextDouble() >= (1 - population.population[index].fitness) / maxFitness);
        return population.population[index].gene;
    }
}
