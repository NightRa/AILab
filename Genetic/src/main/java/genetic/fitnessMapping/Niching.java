package genetic.fitnessMapping;

import genetic.Metric;
import genetic.types.Gene;
import genetic.types.Population;

public class Niching<A> implements FitnessMapping<A> {
    public final double alpha;
    public final double sigmaShare;
    public final Metric<A> metric;

    public Niching(double alpha, double sigmaShare, Metric<A> metric) {
        this.alpha = alpha;
        this.sigmaShare = sigmaShare;
        this.metric = metric;
    }

    @Override
    public double mapFitness(Population<A> population, Gene<A> gene) {
        double sumOfSharingFunc = 0;
        for (int i = 0; i < population.population.length; i++){
            double distance = metric.distance(gene.gene, population.population[i].gene);
            if (distance < sigmaShare)
                sumOfSharingFunc += 1 - Math.pow(distance / sigmaShare, alpha);
        }
        return 1 - ((1 - gene.fitness) / sumOfSharingFunc);
    }
}
