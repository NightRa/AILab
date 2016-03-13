package genetic.selection;

import genetic.types.Population;
import scala.Tuple2;

import java.util.Random;

public interface SelectionStrategy {
    <A> Tuple2<A,A> selectParents(Population<A> population, Random rand);
}
