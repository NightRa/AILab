package string;

import java.util.Random;

public class StringHeuristics {
    public static double heuristic1(char[] elem, char[] target) {
        assert elem.length == target.length;
        int fitness = 0;
        for (int i = 0; i < elem.length; i++) {
            fitness += Math.abs(elem[i] - target[i]);
        }
        return (double) fitness / (91*target.length);
    }

    public static double heuristic2(char[] elem, char[] target) {
        assert elem.length == target.length;
        int fitness = 0;
        for (int i = 0; i < elem.length; i++) {
            fitness += invIndicator(elem[i] == target[i]);
        }
        return (double) fitness / target.length;
    }

    public static double heuristic3(char[] elem, char[] target, int containsWeight, int eqWeight) {
        assert elem.length == target.length;
        double fitness = 0;
        for (int i = 0; i < elem.length; i++) {
            int contains = invIndicator(strContains(target, elem[i]));
            int eq = invIndicator(elem[i] == target[i]);

            fitness += (double) (containsWeight * contains + eqWeight * eq) / (containsWeight + eqWeight);
        }
        return fitness / target.length;
    }

    public static char[] mate(char[] x, char[] y, Random rand) {
        assert x.length == y.length;

        int pos = rand.nextInt(x.length);
        char[] str = new char[x.length];
        System.arraycopy(x, 0, str, 0, pos);
        System.arraycopy(y, pos, str, pos, y.length - pos);
        return str;
    }

    public static boolean strContains(char[] target, char c) {
        for (char targetChar : target) {
            if (targetChar == c)
                return true;
        }
        return false;
    }

    public static int invIndicator(boolean b) {
        return b ? 0 : 1;
    }

    public static int indicator(boolean b) {
        return b ? 1 : 0;
    }


}
