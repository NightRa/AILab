package genetic.localOptima;

import genetic.types.Population;

public class IgnoreLocalOptima implements LocalOptimaSignal {
    @Override
    public <A> boolean isInLocalOptima(Population<A> population) {
        return false;
    }
}
