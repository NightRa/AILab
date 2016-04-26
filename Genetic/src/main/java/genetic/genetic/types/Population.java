package genetic.genetic.types;

import genetic.genetic.Genetic;
import genetic.genetic.fitnessMapping.FitnessMapping;
import genetic.util.RunningStat;

public class Population<A> {
    public Gene<A>[] population;
    private double minFitness;
    private double maxFitness;
    private double avg;
    private double stdDev;

    public Population(Gene<A>[] population) {
        this.population = population;
    }

    public void calc_fitness(Genetic<A> alg, FitnessMapping[] mappings) {
        avg = -1;
        stdDev = -1;
        minFitness = Double.POSITIVE_INFINITY;
        maxFitness = Double.NEGATIVE_INFINITY;
        for (Gene<A> gene : population) {
            gene.fitness = alg.fitness(gene.gene);
            if (gene.fitness < minFitness) minFitness = gene.fitness;
            if (gene.fitness > maxFitness) maxFitness = gene.fitness;
        }
        for (Gene<A> gene : population) {
            for (FitnessMapping mapping : mappings) {
                gene.fitness = mapping.mapFitness(alg.metric(), this, gene);
            }
        }
    }

    // Was very performance critical
    public double bestMinFitness() {
        return minFitness;
    }

    public double worstMaxFitness() {
        return maxFitness;
    }

    private void calc_statistics() {
        if(avg == -1 || stdDev == -1) {
            RunningStat stat = new RunningStat();
            for (Gene<A> gene: population) {
                stat.push(gene.fitness);
            }
            avg = stat.mean();
            stdDev = stat.standardDeviation();
        }
    }

    public double fitnessAvg() {
        calc_statistics();
        return avg;
    }

    public double fitnessStdDev() {
        calc_statistics();
        return stdDev;
    }

}
