package minConflicts;

import scala.util.Random;

import java.util.Optional;


public final class minConflictsMain {
    public static void main(String[] args){
        int size = 20;
        Random rand = new Random();
        QueenBoard board = QueenBoard.genRandom(size, rand);
        long miliSecs = 500;
        long maxDurationMili = System.nanoTime() + miliSecs * 1000000;
        long start = System.currentTimeMillis();
        Optional<QueenBoard> maybeBoard = MinimalConflicts.minimalConflictsAlg(board, maxDurationMili, rand);
        long end = System.currentTimeMillis();
        if (maybeBoard.isPresent())
            System.out.println("found solution: " + "\n" + maybeBoard.get().showBoard());
        else
            System.out.println("didn't find solution");
        System.out.println();
        System.out.println("Took " + (end - start) + " miliSecs");


    }
}
