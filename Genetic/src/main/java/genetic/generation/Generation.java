package genetic.generation;

import genetic.Genetic;
import genetic.fitnessMapping.FitnessMapping;
import genetic.localOptima.LocalOptimaSignal;
import genetic.mutation.MutationStrategy;
import genetic.survivors.SurvivorSelection;
import genetic.types.Population;
import genetic.selection.ParentSelection;
import util.JavaUtil;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class Generation<A> {
    public final ParentSelection selection;
    public final MutationStrategy mutationStrategy;
    public final SurvivorSelection<A> survivorSelection;
    public final FitnessMapping[] fitnessMappings;
    public final Random rand;

    public Generation(ParentSelection selection,
                      MutationStrategy mutationStrategy,
                      SurvivorSelection<A> survivorSelection,
                      FitnessMapping[] fitnessMappings,
                      Random rand) {
        this.selection = selection;
        this.mutationStrategy = mutationStrategy;
        this.survivorSelection = survivorSelection;
        this.fitnessMappings = fitnessMappings;
        this.rand = rand;
    }

    public void nextGeneration(Genetic<A> alg,
                               Population<A> population,
                               Population<A> buffer) {
        // Necessary for TopSelection & Elitism
        JavaUtil.sortGenes(population.population);

        Function<Integer, Supplier<A>> getChildren = numChildren -> {
            Supplier<A> parents = selection.chooseParents(population, numChildren * 2, rand);
            return () -> {
                A parent1 = parents.get();
                A parent2 = parents.get();
                A child = alg.mate(parent1, parent2);
                return mutationStrategy.mutate(alg, child);
            };
        };
        survivorSelection.selectSurvivors(population, buffer, getChildren);
        buffer.calc_fitness(alg, fitnessMappings);
    }
}
