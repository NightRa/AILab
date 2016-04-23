package genetic;

import genetic.types.Gene;

import java.util.Random;

public interface Genetic<A> {
    double fitness(A gene);

    default double score(Gene<A> gene) {
        return gene.fitness;
    }

    A mate(A x, A y);

    // Allowed to be in place (done on fresh gene generated by mate)
    A mutate(A a);

    Metric<A> metric();

    A randomElement(Random rand);

    String show(A gene);

    boolean showScientific();

    int hash(A gene);
}
