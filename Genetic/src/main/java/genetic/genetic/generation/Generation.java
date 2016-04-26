package genetic.genetic.generation;

import genetic.genetic.Genetic;
import genetic.genetic.fitnessMapping.FitnessMapping;
import genetic.genetic.mutation.MutationStrategy;
import genetic.genetic.selection.ParentSelection;
import genetic.genetic.survivors.SurvivorSelectionStrategy;
import genetic.genetic.survivors.construction.PopulationConstruction;
import genetic.genetic.survivors.construction.PopulationConstructor;
import genetic.genetic.types.Population;
import genetic.util.JavaUtil;

import java.util.Random;
import java.util.function.Supplier;

public class Generation {
    public final ParentSelection selection;
    public final MutationStrategy mutationStrategy;
    public final SurvivorSelectionStrategy[] survivorSelectionStrategies;
    public final PopulationConstruction populationConstruction;
    public final FitnessMapping[] fitnessMappings;

    public Generation(ParentSelection selection,
                      MutationStrategy mutationStrategy,
                      SurvivorSelectionStrategy[] survivorSelectionStrategies, PopulationConstruction populationConstruction, FitnessMapping[] fitnessMappings) {
        this.selection = selection;
        this.mutationStrategy = mutationStrategy;
        this.survivorSelectionStrategies = survivorSelectionStrategies;
        this.populationConstruction = populationConstruction;
        this.fitnessMappings = fitnessMappings;
    }

    public <A> void nextGeneration(Genetic<A> alg,
                                   Population<A> population,
                                   Population<A> buffer,
                                   Random rand) {
        selectSurvivors(alg, population, buffer, rand);

        buffer.calc_fitness(alg, fitnessMappings);
        // Necessary for TopSelection & Elitism
        JavaUtil.sortGenes(buffer.population);
    }

    private <A> Supplier<A> getChildren(int numChildren, Genetic<A> alg, Population<A> population, Random rand) {
        Supplier<A> parents = selection.chooseParents(population, numChildren * 2, rand);
        return () -> {
            A parent1 = parents.get();
            A parent2 = parents.get();
            A child = alg.mate(parent1, parent2);
            return mutationStrategy.mutate(alg, child, rand);
        };
    }

    public <A> void selectSurvivors(Genetic<A> alg, Population<A> population, Population<A> buffer, Random rand) {
        int popSize = population.population.length;
        PopulationConstructor<A> populationConstructor = populationConstruction.beginPopulationConstruction(alg, buffer);


        // Select Survivors
        // Do Elitism, Random Immigrants, etc.
        for (SurvivorSelectionStrategy selection : survivorSelectionStrategies) {
            int amount = Math.min(populationConstructor.spaceRemaining(), selection.desiredSize(popSize));
            selection.populateSurvivors(alg, population, populationConstructor, amount, rand);
        }

        int remaining = populationConstructor.spaceRemaining();

        int childrenTriesMax = remaining + 100;

        // Breeding - put new children in the remaining space.
        Supplier<A> children = getChildren(remaining, alg, population, rand);
        while(populationConstructor.spaceRemaining() > 0 && childrenTriesMax > 0) {
            populationConstructor.putNewGene(children.get());
            childrenTriesMax--;
        }

        // If cannot create any children via combinations of parents, put random elements, should be enough unique of them.
        while(populationConstructor.spaceRemaining() > 0) {
            populationConstructor.putNewGene(alg.randomElement(rand));
        }

    }
}
