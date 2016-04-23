package genetic.survivors.construction;

import genetic.Genetic;
import genetic.types.Population;

public interface PopulationConstruction {
    <A> PopulationConstructor<A> beginPopulationConstruction(Genetic<A> genetic, Population<A> buffer);
}
