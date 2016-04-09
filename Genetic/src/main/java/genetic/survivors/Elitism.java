package genetic.survivors;

import genetic.Genetic;
import genetic.survivors.SurvivorSelection;
import genetic.types.Population;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class Elitism implements SurvivorSelection {
    public final double elitismRate;

    public Elitism(double elitismRate) {
        this.elitismRate = elitismRate;
    }

    public static <A> void elitism(Population<A> population, Population<A> buffer, int elitismAmount) {
        System.arraycopy(population.population, 0, buffer.population, 0, elitismAmount);
    }

    @Override
    public <A> void selectSurvivors(Genetic<A> alg,
                                Population<A> population, Population<A> buffer,
                                Function<Integer, Supplier<A>> getChildren,
                                Random rand) {
        int popSize = population.population.length;
        int elites = (int) (popSize * elitismRate);
        elitism(population, buffer, elites);
        int numChildren = popSize - elites;
        Supplier<A> children = getChildren.apply(numChildren);
        for (int i = elites; i < popSize; i++) {
            buffer.population[i].gene = children.get();
            buffer.population[i].age = 0;
        }
    }
}
