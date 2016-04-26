package genetic.genetic.fitnessMapping;

import genetic.genetic.Metric;
import genetic.genetic.types.Gene;
import genetic.genetic.types.Population;

public class Niching implements FitnessMapping {
    public final double alpha;
    public final double sigmaShare;

    public Niching(double alpha, double sigmaShare) {
        this.alpha = alpha;
        this.sigmaShare = sigmaShare;
    }

    @Override
    public <A> double mapFitness(Metric<A> metric, Population<A> population, Gene<A> gene) {
        double sumOfSharingFunc = 0;
        for (int i = 0; i < population.population.length; i++) {
            double distance = metric.distance(gene.gene, population.population[i].gene);
            if (distance < sigmaShare)
                sumOfSharingFunc += 1 - Math.pow(distance / sigmaShare, alpha);
        }
        return 1 - ((1 - gene.fitness) / sumOfSharingFunc);
    }
}
