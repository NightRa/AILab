package genetic.genetic.survivors.construction;

import genetic.genetic.Genetic;
import genetic.genetic.types.Population;

public interface PopulationConstruction {
    <A> PopulationConstructor<A> beginPopulationConstruction(Genetic<A> genetic, Population<A> buffer);
}
