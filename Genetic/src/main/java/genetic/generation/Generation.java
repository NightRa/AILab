package genetic.generation;

import genetic.Genetic;
import genetic.fitnessMapping.FitnessMapping;
import genetic.mutation.MutationStrategy;
import genetic.selection.ParentSelection;
import genetic.survivors.SurvivorSelection;
import genetic.types.Population;
import util.JavaUtil;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class Generation {
    public final ParentSelection selection;
    public final MutationStrategy mutationStrategy;
    public final SurvivorSelection survivorSelection;
    public final FitnessMapping[] fitnessMappings;

    public Generation(ParentSelection selection,
                      MutationStrategy mutationStrategy,
                      SurvivorSelection survivorSelection,
                      FitnessMapping[] fitnessMappings) {
        this.selection = selection;
        this.mutationStrategy = mutationStrategy;
        this.survivorSelection = survivorSelection;
        this.fitnessMappings = fitnessMappings;
    }

    public <A> void nextGeneration(Genetic<A> alg,
                                   Population<A> population,
                                   Population<A> buffer,
                                   Random rand) {
        Function<Integer, Supplier<A>> getChildren = numChildren -> {
            Supplier<A> parents = selection.chooseParents(population, numChildren * 2, rand);
            return () -> {
                A parent1 = parents.get();
                A parent2 = parents.get();
                A child = alg.mate(parent1, parent2);
                return mutationStrategy.mutate(alg, child, rand);
            };
        };
        survivorSelection.selectSurvivors(alg, population, buffer, getChildren, rand);

        buffer.calc_fitness(alg, fitnessMappings);
        // Necessary for TopSelection & Elitism
        JavaUtil.sortGenes(buffer.population);
    }
}
