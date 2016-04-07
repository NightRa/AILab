package util;

import static java.lang.Math.sqrt;

// Code from Jon D. Cook's blog
public final class RunningStat {
    private int m_n;
    private double m_oldM, m_newM, m_oldS, m_newS;

    public RunningStat() {
        m_n = 0;
    }

    public void clear() {
        m_n = 0;
    }

    public void push(double x) {
        m_n++;

        // See Knuth TAOCP vol 2, 3rd edition, page 232
        if (m_n == 1) {
            m_oldM = m_newM = x;
            m_oldS = 0.0;
        } else {
            m_newM = m_oldM + (x - m_oldM) / m_n;
            m_newS = m_oldS + (x - m_oldM) * (x - m_newM);

            // set up for next iteration
            m_oldM = m_newM;
            m_oldS = m_newS;
        }
    }

    public int numDataValues() {
        return m_n;
    }

    public double mean() {
        return (m_n > 0) ? m_newM : 0.0;
    }

    public double variance() {
        return ((m_n > 1) ? m_newS / (m_n - 1) : 0.0);
    }

    public double standardDeviation() {
        return sqrt(variance());
    }

}
