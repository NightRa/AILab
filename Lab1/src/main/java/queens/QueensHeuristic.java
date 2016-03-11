package queens;

import static java.lang.Math.abs;

public class QueensHeuristic {

    public static int isCollision(int i, int j, int[] permutation) {
        if (abs(i - j) == abs(permutation[i] - permutation[j]))
            return 1;
        else
            return 0;
    }

    public static double queensHeuristic(QueenPermutation gene){
        int sum = 0;
        int size = gene.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                sum += isCollision(i, j, gene.permutation());
                count++;
            }
        }
        return (sum / (double) count);
    }
}
