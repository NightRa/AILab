package genetic;

import java.util.Random;

public interface Genetic<A> {
    double fitness(A gene);

    default double meaningfulFitness(A gene) {
        return fitness(gene);
    }

    A mate(A x, A y);

    // Allowed to be in place (done on fresh gene generated by mate)
    A mutate(A a);

    Metric<A> metric();

    A randomElement(Random rand);

    String show(A gene);

    boolean showScientific();
}
