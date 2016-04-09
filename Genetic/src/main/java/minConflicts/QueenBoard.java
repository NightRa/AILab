package minConflicts;

import scala.util.Random;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.abs;

public final class QueenBoard {
    public final int[] board; // board[i] -> row position of queen at col i

    public final int size() {
        return board.length;
    }

    public QueenBoard(int[] board) {
        this.board = board;
    }

    public String toString() {
        String str = "[";

        for (int i = 0; i < board.length - 1; i++)
            str += board[i] + ", ";
        str += board[board.length - 1];
        str += "]";
        return str;

        //return Stream.of(board).map(x -> x.toString()).collect(Collectors.joining(", ", "[", "]"));
    }

    public static QueenBoard genRandom(int size, Random rand) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++)
            array[i] = rand.nextInt(size);
        return new QueenBoard(array);
    }

    public List<Integer> getConflictCols() {
        List<Integer> cols = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            if (this.hasConflictInCol(i))
                cols.add(i);
        }

        return cols;
    }

    private boolean hasConflictInCol(int col) {
        for (int i = 0; i < size(); i++)
            if (i != col) {
                int thisRow = board[col];
                int thatRow = board[i];
                if (thisRow == thatRow)
                    return true;
                if (abs(thisRow - thatRow) == abs(col - i))
                    return true;
            }
        return false;
    }

    public void moveToBest(int col, Random rand) {
        ArrayList<Integer> conflictsSizes = new ArrayList<>();
        for (int row = 0; row < size(); row++) {
            board[col] = row;
            conflictsSizes.add(getConflictCols().size());
        }
        int min = conflictsSizes.stream().min(Comparator.naturalOrder()).get();
        int minNums = (int) conflictsSizes.stream().filter(x -> x == min).count();
        int minNum = rand.nextInt(minNums);
        for (int i = 0; i < conflictsSizes.size(); i++) {
            if (conflictsSizes.get(i) == min)
                if (minNum == 0) {
                    board[col] = i;
                    return;
                } else
                    minNum--;
        }

    }

    public String showBoard() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++)
                if (board[i] == j)
                    builder.append("# ");
                else
                    builder.append("* ");
            builder.append('\n');
        }

        return builder.toString();
    }
}
