package genetic;

public class Gene<A> {
    public A gene;
    public double fitness;

    public Gene(A gene, double fitness) {
        this.gene = gene;
        this.fitness = fitness;
    }
}
