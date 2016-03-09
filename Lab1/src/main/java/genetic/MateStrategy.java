package genetic;

public interface MateStrategy {
    <A> void mateStrategy(Genetic<A> alg, Population<A> population, Population<A> buffer);
}
