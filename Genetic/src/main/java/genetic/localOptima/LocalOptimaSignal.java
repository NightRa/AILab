package genetic.localOptima;

import genetic.types.Population;

public interface LocalOptimaSignal {
    <A> boolean isInLocalOptima(Population<A> population);
}
