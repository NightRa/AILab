package genetic.genetic.selection;

import genetic.genetic.types.Gene;
import genetic.genetic.types.Population;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// SUS - Stochastic Universal Sampling
public class StochasticUniversalSampling implements ParentSelection {
    private <A> Iterator<A> susIterator(Population<A> population, int size, Random rand) {
        // Calculate the sum of all fitness values.
        double fitnessSum = 0;
        for (Gene<A> candidate : population.population) {
            fitnessSum += (1 - candidate.fitness);
        }
        double startPos = rand.nextDouble() * fitnessSum;
        RoulettePosition initialPosition = consume(startPos, new RoulettePosition(0, 1 - population.population[0].fitness), population);
        double stepSize = fitnessSum / size;
        Supplier<A> supplier = new Supplier<A>() {
            RoulettePosition pos = initialPosition;

            @Override
            public A get() {
                int i = pos.index;
                pos = consume(stepSize, pos, population);
                return population.population[i].gene;
            }
        };
        List<A> parents = Stream.generate(supplier).limit(size).collect(Collectors.toList());
        Collections.shuffle(parents);
        return parents.iterator();
    }

    @Override
    public <A> Supplier<A> chooseParents(Population<A> population, int size, Random rand) {
        return new Supplier<A>() {
            Iterator<A> iterator = susIterator(population, size, rand);
            @Override
            public A get() {
                if (iterator.hasNext())
                    return iterator.next();
                else {
                    iterator = susIterator(population, size, rand);
                    return iterator.next();
                }
            }
        };
    }

    private static class RoulettePosition {
        public final int index;
        public final double remaining;

        public RoulettePosition(int index, double remaining) {
            this.index = index;
            this.remaining = remaining;
        }
    }

    private static <A> RoulettePosition consume(double amount, RoulettePosition pos, Population<A> pop) {
        int i = pos.index;
        double remaining = pos.remaining;
        while (remaining <= amount) {
            int nextIndex = (i + 1) % pop.population.length;
            amount = amount - remaining;
            i = nextIndex;
            remaining = 1 - pop.population[nextIndex].fitness;
        }
        return new RoulettePosition(i, remaining - amount);
    }
}
