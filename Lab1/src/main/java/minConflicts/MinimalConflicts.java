package minConflicts;

import scala.util.Random;

import java.util.List;
import java.util.Optional;

/**
 * Created by yuval on 3/15/2016.
 */
public class MinimalConflicts {

    public static Optional<QueenBoard> minimalConflictsAlg(QueenBoard startingBoard, long endTimeInNano, Random rnd){
        while(System.nanoTime() < endTimeInNano){
            List<Integer> conflictCols = startingBoard.getConflictCols();
            if (conflictCols.size() == 0)
                return Optional.of(startingBoard);
            int col = conflictCols.get(rnd.nextInt(conflictCols.size()));
            startingBoard.moveToBest(col, rnd);
        }

        return Optional.empty();
    }

}
