package genetic.localOptima;

import genetic.Metric;
import genetic.types.Population;

public class DistanceSimilarityDetector implements LocalOptimaSignal {
    public final double distanceThresh;

    public DistanceSimilarityDetector(double distanceThresh) {
        this.distanceThresh = distanceThresh;
    }

    @Override
    public <A> boolean isInLocalOptima(Metric<A> metric, Population<A> population) {
        int m_n = 0;
        double m_oldM = 0, m_newM = 0, m_oldS = 0, m_newS = 0;
        for (int i = 0; i < population.population.length; i++) {
            double x = metric.distance(population.population[0].gene, population.population[i].gene);
            m_n++;

            m_newM = m_oldM + (x - m_oldM) / m_n;
            m_newS = m_oldS + (x - m_oldM) * (x - m_newM);

            // set up for next iteration
            m_oldM = m_newM;
            m_oldS = m_newS;
        }

        double variance = ((m_n > 1) ? m_newS / (m_n - 1) : 0.0);
        double stdDev = Math.sqrt(variance);
        return stdDev < distanceThresh;
    }

}
