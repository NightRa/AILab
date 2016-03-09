package genetic;

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
    public <A> void mateStrategy(Genetic<A> alg, Population<A> population, Population<A> buffer) {
        int popSize = population.population.length;
        int elites = (int) (popSize * ElitismRate);
        elitism(population, buffer, elites);
        for (int i = elites; i < popSize; i++) {
            int i1 = rand.nextInt(popSize / 2);
            int i2 = rand.nextInt(popSize / 2);
            buffer.population[i].gene = alg.mate(population.population[i1].gene, population.population[i2].gene);
            if(rand.nextFloat() < MutationRate)
                buffer.population[i].gene = alg.mutate(buffer.population[i].gene);
        }
    }

    public static <A> void elitism(Population<A> population, Population<A> buffer, int elitismAmount) {
        System.arraycopy(population.population, 0, buffer.population, 0, elitismAmount);
    }

}
