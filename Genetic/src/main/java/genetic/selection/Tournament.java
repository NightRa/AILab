package genetic.selection;

import genetic.types.Population;

import java.util.Random;

public class Tournament extends IndependentSelection {
    public final int tournamentSize;

    public Tournament(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public <A> A chooseSingleParent(Population<A> population, Random rand) {
        assert tournamentSize > 0;
        int popSize = population.population.length;

        int index = Integer.MAX_VALUE;
        for (int i = 0; i < tournamentSize; i++) {
            int newIndex = rand.nextInt(popSize);
            if(newIndex < index) index = newIndex;
        }

        return population.population[index].gene;
    }
}
