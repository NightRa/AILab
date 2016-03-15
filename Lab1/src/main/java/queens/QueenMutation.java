package queens;


import scala.util.Random;
import util.JavaUtil;

import static java.lang.Math.*;

public final class QueenMutation {
    public static void displacement(int[] array, Random rand){
        int index1 = rand.nextInt(array.length);
        int index2 = rand.nextInt(array.length);
        int shift = rand.nextInt(array.length - 1);

        insert(array, index1, index2, shift);
    }

    public static void exchange(int[] array, Random rand){
        int index1 = rand.nextInt(array.length);
        int index2 = rand.nextInt(array.length);

        swap(array, index1, index2);
    }

    public static void insertion(int[] array, Random rand){
        int index = rand.nextInt(array.length);
        int min = index;
        int max = min + 1;
        int shift = rand.nextInt(array.length - 1);

        insert(array, min, max, shift);
    }

    public static void simpleInversion(int[] array, Random rand){
        int index1 = rand.nextInt(array.length);
        int index2 = rand.nextInt(array.length);
        int min = min(index1, index2);
        int max = max(index1, index2);
        inverse(array, min, max);
    }
    public static void complexInversion(int[] array, Random rand){
        int index1 = rand.nextInt(array.length);
        int index2 = rand.nextInt(array.length);
        int min = min(index1, index2);
        int max = max(index1, index2);
        inverse(array, min, max);
        int shift = rand.nextInt(array.length - 1);
        insert(array, min, max, shift);
    }

    public static void scramble(int[] array, Random rand){
        int index1 = rand.nextInt(array.length);
        int index2 = rand.nextInt(array.length);
        int min = min(index1, index2);
        int max = max(index1, index2);
        JavaUtil.shuffleArray(array, rand, min, max);
    }

    private static void inverse(int[] array, int minIndex, int maxIndex){
        while (minIndex < maxIndex)
            swap(array, minIndex++, maxIndex--);
    }

    public static int mod(int num, int base){
        if (num >= 0)
            return num % base;
        else
            return (num % base) + base;
    }
    // !
    // error code
    // %
    // not tale recursive due to a maximum of 2 rec calls
    private static int getCyclicIndex(int[] array, int index){
        return mod(index, array.length);
    }

    private static void insert(int[] array, int blockMinIndex, int blockMaxIndex, int shift){
        while (shift > 0){
            for (int i = getCyclicIndex(array, blockMaxIndex + 1);
                 i != blockMinIndex;
                 i = getCyclicIndex(array, i - 1))
                swap(array, i, getCyclicIndex(array, i - 1));
            shift--;
        }
    }

    private static void swap (int[] a, int i1, int i2){
        int temp = a[i1];
        a[i1] = a[i2];
        a[i2] = temp;
    }
}
