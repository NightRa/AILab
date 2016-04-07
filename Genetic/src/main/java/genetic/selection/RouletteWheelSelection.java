package genetic.selection;

import genetic.fitnessMapping.FitnessMapping;
import genetic.types.Gene;
import genetic.types.Population;

import java.util.Random;

// Implemented using Stochastic Acceptance in O(1):
//    http://arxiv.org/abs/1109.3627
public class RouletteWheelSelection extends IndependentSelection {

    // Was very performance critical
    private static <A> double maxFitness(Population<A> population) {
        double min = Double.POSITIVE_INFINITY;
        for (Gene<A> gene : population.population) {
            if (gene.fitness < min) min = gene.fitness;
        }
        return 1 - min;
    }


    @Override
    public <A> A chooseSingleParent(Population<A> population, Random rand) {
        int popSize = population.population.length;
        double maxFitness = maxFitness(population);
        int index;
        do {
            index = rand.nextInt(popSize); // 1/N probability to choose anyone
        } while (rand.nextDouble() >= (1 - population.population[index].fitness) / maxFitness);
        return population.population[index].gene;
    }
}
