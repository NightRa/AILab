package genetic.fitnessMapping;

import genetic.Metric;
import genetic.types.Gene;
import genetic.types.Population;

public class Aging implements FitnessMapping {
    public final int matureAt;
    // Influence: multiplied by a number in the range [1-agingInfluence, 1]
    public final double agingInfluence;

    public Aging(int matureAt, double agingInfluence) {
        this.matureAt = matureAt;
        this.agingInfluence = agingInfluence;
    }

    @Override
    public <A> double mapFitness(Metric<A> metric, Population<A> population, Gene<A> gene) {
        int age = gene.age;
        double fitness = gene.fitness;
        if (matureAt == 0) return fitness;
        else if (age > 2 * matureAt) return fitness * (1 - agingInfluence);
            // Scale x such that matureAt goes to 0.5.
        else
            return fitness * f(1 - agingInfluence, (double) age / (2 * matureAt));
    }

    // A function on [0, 1] that intersects (0,h), (0.5, 1), (1,h)
    private static double f(double h, double x) {
        double a = 4 * h - 4;
        double b = -a;
        double c = h;
        return a * x * x + b * x + c;
    }
}
