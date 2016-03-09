package genetic;

public interface Genetic<A> {
    double fitness(A gene);
    A mate(A x, A y);
    A mutate(A a);
}
