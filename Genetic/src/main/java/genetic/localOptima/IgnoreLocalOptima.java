package genetic.localOptima;

import genetic.types.Population;

public class IgnoreLocalOptima<A> implements LocalOptimaSignal<A> {
    @Override
    public boolean isInLocalOptima(Population<A> population) {
        return false;
    }
}
