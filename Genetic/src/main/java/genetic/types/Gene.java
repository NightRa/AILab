package genetic.types;

import java.util.Comparator;

public class Gene<A> {
    public A gene;
    public double fitness;

    public Gene(A gene, double fitness) {
        this.gene = gene;
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return gene + ", fitness = " + fitness;
    }

    public static Comparator<Gene<?>> fitnessComparator = Comparator.comparing(gene -> gene.fitness);
}
