package genetic.genetic;

public interface Metric<A> {
    double distance(A x, A y);
}
