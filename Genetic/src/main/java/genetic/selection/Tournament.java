package genetic.selection;

import genetic.types.Gene;
import genetic.types.Population;

import java.util.Random;
import java.util.stream.Stream;

public class Tournament extends IndependentSelection {
    public final int tournamentSize;

    public Tournament(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public <A> A chooseSingleParent(Population<A> population, Random rand) {
        assert tournamentSize > 0;
        Gene<A>[] pop = population.population;
        return Stream.generate(() -> pop[rand.nextInt(pop.length)])
                .limit(tournamentSize)
                .min(Gene.fitnessComparator)
                .get().gene;
    }
}
