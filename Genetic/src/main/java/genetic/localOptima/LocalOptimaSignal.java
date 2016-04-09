package genetic.localOptima;

import genetic.Metric;
import genetic.types.Population;

public interface LocalOptimaSignal {
    <A> boolean isInLocalOptima(Metric<A> metric, Population<A> population);
}
