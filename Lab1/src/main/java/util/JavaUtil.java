package util;

import genetic.types.Gene;
import scala.util.Random;

import java.util.Arrays;

public final class JavaUtil {

    public static <A> void sortGenes(Gene<A>[] arr) {
        Arrays.sort(arr, (x, y) -> comp(x.fitness, y.fitness));
    }

    private static int comp(double x, double y) {
        double diff = x - y;
        if (diff < 0) return -1;
        else if (diff == 0) return 0;
        else return 1;
    }

    public static void shuffleArray(int[] ar, Random rnd) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }


    public static void swapValues(int[] array, int a, int b) {
        int i = indexOf(array, a);
        int j = indexOf(array, a);
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static int indexOf(int[] array, int num){
        for (int i = 0; i < array.length; i++)
            if (array[i] == num)
                return i;
        throw new IllegalArgumentException("the array does not contain num: " + num);
    }

    public static boolean containsValue(int[] array, int value){
        for (int i = 0; i < array.length; i++)
            if (array[i] == value)
                return true;
        return false;
    }

    public static int firstIndexOf(int value, int[] array) {
        for (int i = 0; i < array.length; i++)
            if (array[i] == value)
                return i;
        return -1;
    }
}
