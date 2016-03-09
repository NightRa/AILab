package genetic;

import params.Params;

public interface GeneticMain<A> {
    int intsSize();
    int intsMax();
    int doublesSize();
    GeneticAlg<A> alg(Params params, double maxTime);
}
