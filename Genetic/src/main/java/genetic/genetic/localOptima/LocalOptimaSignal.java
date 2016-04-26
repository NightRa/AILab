package genetic.genetic.localOptima;

import genetic.genetic.Metric;
import genetic.genetic.types.Population;

public interface LocalOptimaSignal {
    <A> boolean isInLocalOptima(Metric<A> metric, Population<A> population);
}
