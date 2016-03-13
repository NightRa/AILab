package genetic.selection;

import genetic.types.Population;
import scala.Tuple2;

import java.util.Random;

public class TopSelection implements SelectionStrategy {
    public final double topRatio;

    public TopSelection(double topRatio) {
        this.topRatio = topRatio;
    }

    @Override
    public <A> Tuple2<A, A> selectParents(Population<A> population, Random rand) {
        int popSize = population.population.length;
        int selectionRange = Math.max(Math.min((int) (popSize * topRatio), popSize), 1);
        int i1 = rand.nextInt(selectionRange);
        int i2 = rand.nextInt(selectionRange);
        return new Tuple2<>(population.population[i1].gene, population.population[i2].gene);
    }


}
