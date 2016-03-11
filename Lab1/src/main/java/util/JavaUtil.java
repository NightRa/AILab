package util;

import genetic.Gene;
import scala.util.Random;

import java.util.Arrays;

public class JavaUtil {

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

}
