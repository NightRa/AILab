package genetic.survivors;

import genetic.Genetic;
import genetic.types.Population;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static genetic.survivors.Elitism.elitism;

public class ElitismRandomImmigrants<A> implements SurvivorSelection<A> {

    public final double randomImmigrantsPercent;
    public final double elitismRate;

    public ElitismRandomImmigrants(double randomImmigrantsPercent, double elitismRate) {
        this.randomImmigrantsPercent = randomImmigrantsPercent;
        this.elitismRate = elitismRate;
    }

    @Override
    public void selectSurvivors(Genetic<A> alg, Population<A> population, Population<A> buffer, Function<Integer, Supplier<A>> getChildren, Random rand) {
        int popSize = population.population.length;
        int elites = (int) (popSize * elitismRate);
        elitism(population, buffer, elites);

        int immigrants = (int) randomImmigrantsPercent * popSize;
        for (int i = elites; i < (elites + immigrants) && i < popSize; i++) {
            buffer.population[i].gene = alg.randomElement(rand);
        }

        int numChildren = Math.max(popSize - elites - immigrants, 0);
        Supplier<A> children = getChildren.apply(numChildren);
        for (int i = elites; i < popSize; i++) {
            buffer.population[i].gene = children.get();
        }
    }
}
