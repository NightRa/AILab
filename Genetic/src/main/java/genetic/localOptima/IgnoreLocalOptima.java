package genetic.localOptima;

import genetic.Metric;
import genetic.types.Population;

public class IgnoreLocalOptima implements LocalOptimaSignal {
    @Override
    public <A> boolean isInLocalOptima(Metric<A> metric, Population<A> population) {
        return false;
    }
}
