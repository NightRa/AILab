package genetic.selection;

import genetic.types.Gene;
import genetic.types.Population;
import scala.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// SUS - Stochastic Universal Sampling
public class StochasticUniversalSampling implements SelectionStrategy {
    @Override
    public <A> Iterator<A> chooseParents(Population<A> population, int size, Random rand) {
        // Calculate the sum of all fitness values.
        double fitnessSum = 0;
        for (Gene<A> candidate : population.population) {
            fitnessSum += (1 - candidate.fitness);
        }
        double startPos = rand.nextDouble() * fitnessSum;
        RoulettePosition initialPosition = consume(startPos, new RoulettePosition(0, 1 - population.population[0].fitness), population);
        double stepSize = fitnessSum / size;

        Iterator<A> iterator =  new Iterator<A>() {
            RoulettePosition pos = initialPosition;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public A next() {
                int i = pos.index;
                pos = consume(stepSize, pos, population);
                return population.population[i].gene;
            }
        };
        List<A> parents = Stream.generate(iterator::next).limit(size).collect(Collectors.toList());
        Collections.shuffle(parents);
        return parents.iterator();
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
