import java.util.Random;

public class StringHeuristics {
    public static int heuristic1(char[] elem, char[] target) {
        assert elem.length == target.length;
        int fitness = 0;
        for (int i = 0; i < elem.length; i++) {
            fitness += Math.abs(elem[i] - target[i]);
        }
        return fitness;
    }

    public static int heuristic2(char[] elem, char[] target) {
        assert elem.length == target.length;
        int fitness = 0;
        for (int i = 0; i < elem.length; i++) {
            fitness += invIndicator(elem[i] == target[i]);
        }
        return fitness;
    }

    public static int invIndicator(boolean b) {
        return b ? 0 : 1;
    }

    public static int indicator(boolean b) {
        return b ? 1 : 0;
    }

    public static int heuristic3(char[] elem, char[] target, int containsWeight, int eqWeight) {
        assert elem.length == target.length;
        int fitness = 0;
        for (int i = 0; i < elem.length; i++) {
            int contains = indicator(strContains(target, elem[i]));
            int eq = indicator(elem[i] == target[i]);

            fitness += (containsWeight + eqWeight) - containsWeight * contains - eqWeight * eq;
        }
        return fitness;
    }

    public static boolean strContains(char[] target, char c) {
        for (char targetChar : target) {
            if (targetChar == c)
                return true;
        }
        return false;
    }

    public static char[] mate(char[] x, char[] y, Random rand) {
        assert x.length == y.length;

        int pos = rand.nextInt(x.length);
        char[] str = new char[x.length];
        for (int i = 0; i < pos; i++) {
            str[i] = x[i];
        }
        for (int i = pos; i < y.length; i++) {
            str[i] = y[i];
        }
        return str;
    }
}
