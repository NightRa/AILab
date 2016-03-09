package util;

import genetic.Gene;

import java.util.Arrays;

public class JavaUtil {
    public static <A> void sortGenes(Gene<A>[] arr) {
        Arrays.sort(arr, (x,y) -> x.fitness - y.fitness);
    }

}
