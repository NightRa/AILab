package genetic.minConflicts;

import scala.util.Random;

import java.util.List;
import java.util.Optional;

/**
 * Created by yuval on 3/15/2016.
 */
public class MinimalConflicts {

    // 33.5 ms for board of size 10
    public static Optional<QueenBoard> minimalConflictsAlgTimeLimited(QueenBoard startingBoard, double timeInSec, Random rnd) {
        long endTime = System.nanoTime() + (long) (timeInSec * 1e9);
        return minimalConflictsAlg(startingBoard, endTime, rnd);
    }

    public static Optional<QueenBoard> minimalConflictsAlg(QueenBoard startingBoard, long endTimeInNano, Random rnd) {
        while (System.nanoTime() < endTimeInNano) {
            List<Integer> conflictCols = startingBoard.getConflictCols();
            if (conflictCols.size() == 0)
                return Optional.of(startingBoard);
            int col = conflictCols.get(rnd.nextInt(conflictCols.size()));
            startingBoard.moveToBest(col, rnd);
        }

        return Optional.empty();
    }

}
