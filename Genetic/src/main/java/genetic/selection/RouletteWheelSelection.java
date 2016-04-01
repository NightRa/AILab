package genetic.selection;

import genetic.types.Gene;
import genetic.types.Population;

import java.util.Random;
import java.util.stream.Stream;

// Implemented using Stochastic Acceptance in O(1):
//    http://arxiv.org/abs/1109.3627
public class RouletteWheelSelection implements SelectionStrategy {

    @Override
    public <A> A chooseParent(Population<A> population, Random rand) {
        int popSize = population.population.length;
        double maxFitness =
                1 - Stream.of(population.population)
                        .min(Gene.fitnessComparator).get().fitness;
        int index;
        do {
            index = rand.nextInt(popSize); // 1/N probability to choose anyone
        } while (rand.nextDouble() >= (1 - population.population[index].fitness) / maxFitness);
        return population.population[index].gene;
    }
}
