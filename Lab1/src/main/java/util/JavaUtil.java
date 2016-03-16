package util;

import genetic.types.Gene;
import scala.util.Random;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Comparator.comparing;

public final class JavaUtil {

    public static <A> void sortGenes(Gene<A>[] arr) {
        Arrays.parallelSort(arr, comparing(x -> x.fitness));
    }

    public static void shuffleArray(int[] ar, Random rnd) {
        shuffleArray(ar, rnd, 0, ar.length - 1);
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

    public static int firstDifIndex(int[] array1, ArrayList<Integer> array2) {
        int[] a1Cop = array1.clone();
        Arrays.sort(a1Cop);
        Integer[] a2Cop = (Integer[]) array2.toArray();
        Arrays.sort(a2Cop);
        for (int i = 0; i < a1Cop.length; i++)
            if (a1Cop[i] != a2Cop[i])
                return i;

        throw new IllegalArgumentException("I have no power for this.. maybe implementation error");
    }

    public static void shuffleArray(int[] ar, Random rnd, int min, int max) {
        for (int i = max; i >= min; i--) {
            int index = rnd.nextInt(max + 1 - min) + min;
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
