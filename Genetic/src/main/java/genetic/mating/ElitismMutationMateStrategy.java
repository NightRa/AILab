package genetic.mating;

import genetic.Genetic;
import genetic.selection.SelectionStrategy;
import genetic.types.Population;

import java.util.Iterator;
import java.util.Random;

public class ElitismMutationMateStrategy implements MateStrategy {
    public final double ElitismRate;
    public final double MutationRate;
    public final Random rand;

    public ElitismMutationMateStrategy(double elitismRate, double mutationRate, Random rand) {
        ElitismRate = elitismRate;
        MutationRate = mutationRate;
        this.rand = rand;
    }

    @Override
    public <A> void mateStrategy(Genetic<A> alg, SelectionStrategy selection, Population<A> population, Population<A> buffer, boolean isInLocalOptimum) {
        int popSize = population.population.length;
        int elites = (int) (popSize * ElitismRate);
        elitism(population, buffer, elites);
        int children = popSize - elites;
        Iterator<A> parents = selection.chooseParents(population, children * 2, rand);
        for (int i = elites; i < popSize; i++) {
            A parent1 = parents.next();
            A parent2 = parents.next();
            buffer.population[i].gene = alg.mate(parent1, parent2);
            if(rand.nextFloat() < MutationRate)
                buffer.population[i].gene = alg.mutate(buffer.population[i].gene);
        }
    }

    public static <A> void elitism(Population<A> population, Population<A> buffer, int elitismAmount) {
        System.arraycopy(population.population, 0, buffer.population, 0, elitismAmount);
    }

}
