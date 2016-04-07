package genetic;

import genetic.types.Population;
import params.NamedParams;
import params.Params;
import scala.Tuple2;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public abstract class GeneticMain<A> {
    public final long seed;
    protected Random rand;

    public GeneticMain() {
        this.seed = 8682522807148012L ^ System.nanoTime();
        this.rand = new Random(seed);
    }

    public abstract double MaxTime();
    public abstract int printEvery();

    public abstract NamedParams defaultParams();
    public abstract int intsMax();
    public abstract GeneticAlg<A> alg(Params params);

    public abstract String name();

    public final int intsSize() {
        return defaultParams().ints().length;
    }
    public final int doublesSize() {
        return defaultParams().doubles().length;
    }

    public void main(String[] args) {
        GeneticAlg<A> alg = alg(defaultParams().toParams());
        long start = System.currentTimeMillis();
        Tuple2<Population<A>, Object> res = alg.run(printEvery(), MaxTime());
        long end = System.currentTimeMillis();
        long time = end - start;

        Population<A> population = res._1;
        int iterations = (Integer) res._2;

        System.out.println(defaultParams());
        System.out.println("Best 5:");
        System.out.println(Stream.of(population.population).sorted(comparing(x -> x.fitness)).limit(5).map(x -> x.show(alg.genetic())).collect(Collectors.joining("\n")));
        System.out.println(time + "ms, " + iterations + " iterations\t\t\t\tseed: " + seed);
    }
}
