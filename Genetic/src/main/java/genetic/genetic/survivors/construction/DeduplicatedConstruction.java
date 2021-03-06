package genetic.genetic.survivors.construction;

import genetic.genetic.Genetic;
import genetic.genetic.types.Population;

public class DeduplicatedConstruction implements PopulationConstruction {
    @Override
    public <A> PopulationConstructor<A> beginPopulationConstruction(Genetic<A> genetic, Population<A> buffer) {
        return new DeduplicatedConstructor<>(genetic, buffer);
    }
}
