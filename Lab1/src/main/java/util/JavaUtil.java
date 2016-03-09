package util;

import genetic.Gene;

import java.util.Arrays;

public class JavaUtil {

    public static <A> void sortGenes(Gene<A>[] arr) {
        Arrays.sort(arr, (x,y) -> comp(x.fitness, y.fitness));
    }

    private static int comp(double x, double y) {
        double diff = x - y;
        if(diff < 0) return -1;
        else if(diff == 0) return 0;
        else return 1;
    }

}
