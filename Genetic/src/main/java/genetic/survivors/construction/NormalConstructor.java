package genetic.survivors.construction;

import genetic.types.Population;

public class NormalConstructor<A> extends PopulationConstructor<A> {
    private int i;

    @Override
    public int spaceRemaining() {
        return buffer.population.length - i;
    }

    public NormalConstructor(Population<A> buffer) {
        super(buffer);
        this.i = 0;
    }

    @Override
    protected int putGene(A gene) {
        this.buffer.population[i].gene = gene;
        return i++;
    }
}
