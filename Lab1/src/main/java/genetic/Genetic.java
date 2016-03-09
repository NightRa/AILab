package genetic;

public interface Genetic<A> {
    int fitness(A gene);
    A mate(A x, A y);
    void mateStrategy(Population<A> population, Population<A> buffer);
    A mutate(A a);
}
