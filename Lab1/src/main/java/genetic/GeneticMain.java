package genetic;

import genetic.types.Population;
import params.Params;
import scala.Tuple2;

import java.util.Random;

public abstract class GeneticMain<A> {
    protected long seed;
    protected Random rand;

    public GeneticMain() {
        this.seed = 8682522807148012L ^ System.nanoTime();
        this.rand = new Random(seed);
    }

    public abstract double MaxTime();
    public abstract boolean fullOutput();

    public abstract Params defaultParams();
    public abstract int intsMax();
    public abstract GeneticAlg<A> alg(Params params, double maxTime);

    public final int intsSize() {
        return defaultParams().ints().length;
    }
    public final int doublesSize() {
        return defaultParams().ints().length;
    }

    public void main(String[] args) {
        long start = System.currentTimeMillis();
        Tuple2<?, Object> res = alg(defaultParams(), MaxTime()).run(fullOutput());
        long end = System.currentTimeMillis();
        long time = end - start;

        int iterations = (Integer) res._2;

        System.out.println(defaultParams());
        System.out.println(time + "ms, " + iterations + " iterations\t\t\t\tseed: " + seed);
    }
}
