package genetic.genetic.localOptima;

import genetic.genetic.Metric;
import genetic.genetic.types.Population;

public class IgnoreLocalOptima implements LocalOptimaSignal {
    @Override
    public <A> boolean isInLocalOptima(Metric<A> metric, Population<A> population) {
        return false;
    }
}
