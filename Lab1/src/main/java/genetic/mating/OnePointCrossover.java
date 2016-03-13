package genetic.mating;

import genetic.Genetic;
import genetic.types.Population;
import genetic.selection.SelectionStrategy;
import scala.Tuple2;

import java.util.Random;

public class OnePointCrossover implements MateStrategy {
    public final double ElitismRate;
    public final double MutationRate;
    public final Random rand;

    public OnePointCrossover(double elitismRate, double mutationRate, Random rand) {
        ElitismRate = elitismRate;
        MutationRate = mutationRate;
        this.rand = rand;
    }

    @Override
    public <A> void mateStrategy(Genetic<A> alg, SelectionStrategy selection, Population<A> population, Population<A> buffer) {
        int popSize = population.population.length;
        int elites = (int) (popSize * ElitismRate);
        elitism(population, buffer, elites);
        for (int i = elites; i < popSize; i++) {
            Tuple2<A,A> parents = selection.selectParents(population, rand);
            buffer.population[i].gene = alg.mate(parents._1, parents._2);
            if(rand.nextFloat() < MutationRate)
                buffer.population[i].gene = alg.mutate(buffer.population[i].gene);
        }
    }

    public static <A> void elitism(Population<A> population, Population<A> buffer, int elitismAmount) {
        System.arraycopy(population.population, 0, buffer.population, 0, elitismAmount);
    }

}
