package genetic.selection;

import genetic.types.Gene;
import genetic.types.Population;
import util.JavaUtil;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class Tournament implements SelectionStrategy {
    public final int tournamentSize;

    public Tournament(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public <A> A chooseParent(Population<A> population, Random rand) {
        assert tournamentSize > 0;
        Gene<A>[] pop = population.population;
        return Stream.generate(() -> pop[rand.nextInt(pop.length)])
                .limit(tournamentSize)
                .min(Gene.fitnessComparator)
                .get().gene;
    }

}
