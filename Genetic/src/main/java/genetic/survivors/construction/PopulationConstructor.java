package genetic.survivors.construction;

import genetic.types.Population;

public abstract class PopulationConstructor<A> {
    protected Population<A> buffer;

    public PopulationConstructor(Population<A> buffer) {
        this.buffer = buffer;
    }

    public abstract int spaceRemaining();

    /**
     * Possibly puts the gene in the next generation,
     * returning it's index if put,
     * -1 otherwise.
    **/
    protected abstract int putGene(A gene);

    public int putOldGene(A gene) {
        int i = putGene(gene);
        if(i != -1) {
            buffer.population[i].age += 1;
        }
        return i;
    }

    public int putNewGene(A gene) {
        int i = putGene(gene);
        if(i != -1) {
            buffer.population[i].age = 0;
        }
        return i;
    }
}
