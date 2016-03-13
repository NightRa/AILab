package genetic.selection;

import genetic.types.Gene;
import genetic.types.Population;
import scala.Tuple2;

import java.util.Random;

public class Tournament implements SelectionStrategy {
    public final int tournamentParticipants;

    public Tournament(int tournamentParticipants) {
        this.tournamentParticipants = tournamentParticipants;
    }


    @Override
    public <A> Tuple2<A, A> selectParents(Population<A> population, Random rand) {
        int popSize = population.population.length;
        Gene<A> elem1 = population.population[rand.nextInt(popSize)];
        Gene<A> elem2 = population.population[rand.nextInt(popSize)];
        Gene<A> max1;
        Gene<A> max2;
        if (elem1.fitness < elem2.fitness) {
            max1 = elem1;
            max2 = elem2;
        } else {
            max1 = elem2;
            max2 = elem1;
        }

        for (int i = 2; i < tournamentParticipants; i++) {
            Gene<A> elem = population.population[rand.nextInt(popSize)];
            if (elem.fitness < max1.fitness) {
                max2 = max1;
                max1 = elem;
            } else if (elem.fitness < max2.fitness) {
                max2 = elem;
            }
        }
        return new Tuple2<>(max1.gene, max2.gene);
    }
}
