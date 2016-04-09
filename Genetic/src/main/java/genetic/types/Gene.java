package genetic.types;

import genetic.Genetic;

import java.util.Comparator;

public class Gene<A> {
    public A gene;
    public double fitness;
    public int age;

    public Gene(A gene, double fitness) {
        this.gene = gene;
        this.fitness = fitness;
        this.age = 0;
    }

    @Override
    public String toString() {
        return gene + ", fitness = " + fitness;
    }

    public String show(Genetic<A> genetic) {
        return genetic.show(gene) + ", fitness = " + fitness;
    }

    public static Comparator<Gene<?>> fitnessComparator = Comparator.comparing(gene -> gene.fitness);
}
