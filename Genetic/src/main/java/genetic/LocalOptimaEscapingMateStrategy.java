package genetic;

import genetic.mating.ElitismMutationMateStrategy;
import genetic.selection.SelectionStrategy;
import genetic.types.Gene;
import genetic.types.Population;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class LocalOptimaEscapingMateStrategy<A> extends ElitismMutationMateStrategy<A> {

    private Optional<RandomImmigrants<A>> randomImmigrants;
    private Optional<Niching<A>> niching;
    private Optional<HyperMutation> hyperMutation;

    public final static class HyperMutation{
        public final double newMutationRate;

        public HyperMutation(double newMutationRate) {
            this.newMutationRate = newMutationRate;
        }
    }

    public final static class Niching<B>{
        public final double alpha;
        public final double sigmaShare;
        public final Metric<B> metric;

        public Niching(double alpha, double sigmaShare, Metric<B> metric) {
            this.alpha = alpha;
            this.sigmaShare = sigmaShare;
            this.metric = metric;
        }
    }

    public final static class RandomImmigrants<A>{
        public final double populationPercent;
        public final Supplier<Gene<A>> generateRandomGene;

        public RandomImmigrants(double populationPercent, Supplier<Gene<A>> generateRandomGene) {
            this.populationPercent = populationPercent;
            this.generateRandomGene = generateRandomGene;
        }
    }



    public LocalOptimaEscapingMateStrategy(double elitismRate,
                                           double mutationRate,
                                           Random rand,
                                           Optional<RandomImmigrants<A>> randomImmigrants,
                                           Optional<Niching<A>> niching,
                                           Optional<HyperMutation> hyperMutation) {
        super(elitismRate,mutationRate, rand);
        this.randomImmigrants = randomImmigrants;
        this.niching = niching;
        this.hyperMutation = hyperMutation;
    }

    @Override
    public void mateStrategy(Genetic<A> alg,
                                 SelectionStrategy selection,
                                 Population<A> population,
                                 Population<A> buffer,
                                 boolean isInLocalOptimum) {
        if (isInLocalOptimum && this.niching.isPresent())
            population = changeToNichingFitness(population, this.niching.get());
        int popSize = population.population.length;
        int elites = (int) (popSize * ElitismRate);
        elitism(population, buffer, elites);
        if (isInLocalOptimum && this.randomImmigrants.isPresent()){
            elites = randomImmigrant(population, buffer, elites, this.randomImmigrants.get());
        }
        int children = popSize - elites;
        Iterator<A> parentsPool = selection.chooseParents(population, children * 2, rand);
        for (int i = elites; i < popSize; i++) {

            A parent1 = parentsPool.next();
            A parent2 = parentsPool.next();
            buffer.population[i].gene = alg.mate(parent1, parent2);

            if (isInLocalOptimum && this.hyperMutation.isPresent()) {
                if (rand.nextFloat() < this.hyperMutation.get().newMutationRate)
                    buffer.population[i].gene = alg.mutate(buffer.population[i].gene);
            } else if(rand.nextFloat() < MutationRate)
                buffer.population[i].gene = alg.mutate(buffer.population[i].gene);
        }
    }

    // O(n) more space - but its negligible. the Algorithm is O(n^2) time... :) (Hi Ilan)
    private static <A> Population<A> changeToNichingFitness(Population<A> population, Niching<A> niching) {
        Gene<A>[] newGenes = new Gene[population.population.length];
        for (int i = 0; i < population.population.length; i++)
            newGenes[i] = new Gene<A>(population.population[i].gene ,calcNichingFitnessForIndex(i, population, niching));
        return new Population<>(newGenes);
    }

    private static <A> double calcNichingFitnessForIndex(int index, Population<A> population, Niching<A> niching) {
        double sumOfSharingFunc = 0;
        for (int i = 0; i < population.population.length; i++){
            double distance = niching.metric.distance(population.population[index].gene, population.population[i].gene);
            if (distance < niching.sigmaShare)
                sumOfSharingFunc += 1 - Math.pow(distance / niching.sigmaShare, niching.alpha);
        }
        return 1 - ((1 - population.population[index].fitness) / sumOfSharingFunc);
    }

    private static <A> int randomImmigrant(Population<A> population, Population<A> buffer, int index, RandomImmigrants<A> randomImmigrants) {
        int populationSize = population.population.length;
        int amount = (int) randomImmigrants.populationPercent * populationSize;
        for (int i = 0; i < amount && index < populationSize; i++){
            buffer.population[index] = randomImmigrants.generateRandomGene.get();
            index++;
        }

        return index;
    }
}
