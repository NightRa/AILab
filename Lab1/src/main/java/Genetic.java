public interface Genetic<A> {
    public int fitness(A gene);
    public A mate(A x, A y);
    public void mateStrategy(Population<A> population, Population<A> buffer);
    public A mutate(A a);
}
