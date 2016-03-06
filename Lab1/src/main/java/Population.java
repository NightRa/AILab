public class Population<A> {
    public Gene<A>[] population;

    public Population(Gene<A>[] population) {
        this.population = population;
    }

    public void calc_fitness(Genetic<A> alg) {
        for(Gene<A> gene: population) {
            gene.fitness = alg.fitness(gene.gene);
        }
    }

}
