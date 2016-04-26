package genetic.genetic.survivors.construction;

import genetic.genetic.Genetic;
import genetic.genetic.types.Population;

import java.util.HashSet;

public class DeduplicatedConstructor<A> extends PopulationConstructor<A> {
    public final Genetic<A> genetic;
    private class Hashed {
        public final A value;

        private Hashed(A value) {
            this.value = value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Hashed hashed = (Hashed) o;

            return value.equals(hashed.value);

        }

        @Override
        public int hashCode() {
            return genetic.hash(value);
        }
    }

    private HashSet<Hashed> elementsSet;
    private int i;

    @Override
    public int spaceRemaining() {
        return buffer.population.length - i;
    }

    public DeduplicatedConstructor(Genetic<A> genetic, Population<A> buffer) {
        super(buffer);
        this.genetic = genetic;
        this.elementsSet = new HashSet<>(buffer.population.length);
        this.i = 0;
    }

    @Override
    protected int putGene(A gene) {
        if(elementsSet.contains(new Hashed(gene))) return -1;
        else {
            elementsSet.add(new Hashed(gene));
            buffer.population[i].gene = gene;
            return i++;
        }
    }
}
