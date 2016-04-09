package queens;

import util.JavaUtil;

import java.util.Random;

public final class QueenMating {

    // partially matched crossover
    public static int[] pmx(int[] a1, int[] a2, Random rand) {
        assert (a1.length == a2.length);
        int size = a1.length;
        int[] newArray = a1.clone();
        assert (newArray != a1);

        int randomIndex = rand.nextInt(size);
        int a1Value = a1[randomIndex];
        int a2Value = a2[randomIndex];
        JavaUtil.swapValues(newArray, a1Value, a2Value);
        return newArray;
    }

    // TODO: improve to O(n)
    // it is slightly different from what described... but well enough
    // complexity: O(n^2), O(1) space
    // ordered crossover
    public static int[] ox(int a1[], int a2[], Random rand) {
        int[] newArray = a1.clone();

        for (int i = 0; i < a1.length; i++)
            if (rand.nextBoolean())
                newArray[i] = -1;

        for (int i = 0; i < a1.length; i++)
            if (!JavaUtil.containsValue(newArray, a2[i])) {
                int index = JavaUtil.firstIndexOf(-1, newArray);
                newArray[index] = a2[i];
            }

        return newArray;
    }

    // cycle crossover
    public static int[] cx(int[] a1, int[] a2, Random rand) {
        int[] newArray = a2.clone();
        int index = rand.nextInt(a1.length);
        int newIndex = index;

        do {
            int value = a1[newIndex];
            newArray[newIndex] = a2[newIndex];
            newIndex = JavaUtil.indexOf(a1, value);
        } while (newIndex != index);

        return newArray;
    }
}
