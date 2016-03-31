package genetic.localOptima;

import genetic.types.Population;

public class StdDevLocalOptimaDetector implements LocalOptimaSignal {
    @Override
    public <A> boolean isInLocalOptima(Population<A> population) {
        return false;
    }
}
