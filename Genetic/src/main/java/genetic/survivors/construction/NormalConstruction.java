package genetic.survivors.construction;

import genetic.Genetic;
import genetic.types.Population;

public class NormalConstruction implements PopulationConstruction {
    @Override
    public <A> PopulationConstructor<A> beginPopulationConstruction(Genetic<A> genetic, Population<A> buffer) {
        return new NormalConstructor<>(buffer);
    }
}
