package genetic.generation;

import java.util.Random;

import static java.lang.System.arraycopy;

public class Crossover {
    // So many copy-pasting to avoid boxing.

    public static byte[] onePointCrossoverBytes(byte[] x, byte[] y, Random rand) {
        assert x.length == y.length;

        int pos = rand.nextInt(x.length);
        byte[] str = new byte[x.length];
        arraycopy(x, 0, str, 0, pos);
        arraycopy(y, pos, str, pos, y.length - pos);
        return str;
    }

    // For Strings
    public static char[] onePointCrossoverString(char[] x, char[] y, Random rand) {
        assert x.length == y.length;

        int pos = rand.nextInt(x.length);
        char[] str = new char[x.length];
        arraycopy(x, 0, str, 0, pos);
        arraycopy(y, pos, str, pos, y.length - pos);
        return str;
    }

    public static char[] twoPointCrossoverString(char[] x, char[] y, Random rand) {
        assert x.length == y.length;

        int len1 = rand.nextInt(x.length);
        int len2 = rand.nextInt(x.length - len1);
        char[] str = new char[x.length];
        arraycopy(x, 0, str, 0, len1);
        arraycopy(y, len1, str, len1, len2);
        arraycopy(x, len1 + len2, str, len1 + len2, x.length - len1 - len2);
        return str;
    }

    public static char[] uniformCrossoverString(char[] x, char[] y, Random rand) {
        assert x.length == y.length;

        char[][] inputs = {x, y};
        char[] str = new char[x.length];
        for (int i = 0; i < x.length; i++) {
            str[i] = inputs[rand.nextInt(2)][i];
        }
        return str;
    }

    // For ints. Damn java generics performance
    public static int[] onePointCrossoverInt(int[] x, int[] y, Random rand) {
        assert x.length == y.length;

        int pos = rand.nextInt(x.length);
        int[] str = new int[x.length];
        arraycopy(x, 0, str, 0, pos);
        arraycopy(y, pos, str, pos, y.length - pos);
        return str;
    }

    public static int[] twoPointCrossoverInt(int[] x, int[] y, Random rand) {
        assert x.length == y.length;

        int len1 = rand.nextInt(x.length);
        int len2 = rand.nextInt(x.length - len1);
        int[] str = new int[x.length];
        arraycopy(x, 0, str, 0, len1);
        arraycopy(y, len1, str, len1, len2);
        arraycopy(x, len1 + len2, str, len1 + len2, x.length - len1 - len2);
        return str;
    }

    public static int[] uniformCrossoverInt(int[] x, int[] y, Random rand) {
        assert x.length == y.length;

        int[][] inputs = {x, y};
        int[] str = new int[x.length];
        for (int i = 0; i < x.length; i++) {
            str[i] = inputs[rand.nextInt(2)][i];
        }
        return str;
    }
}
