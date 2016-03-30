package mcts;

import func.GeneticFuncMain$;
import params.Params;
import queens.GeneticQueenMain$;
import string.GeneticStringMain$;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MCTS {
    static Random r = new Random();
    static int nActions = 2;
    // public static int maxDepth = 1;
    static double epsilon = 1e-6;

    public static double Diversity = 0.5;

    MCTS[] children;
    double nVisits, totValue;
    double totalTime;

    public static MCTS bestNode;

    public final double[] values;

    public static MCTS initMCTS(int size) {
        double[] values = new double[size];
        for (int i = 0; i < size; i++) {
            values[i] = 0.5;
        }
        MCTS mcts = new MCTS(values);
        bestNode = mcts;
        return mcts;
    }

    private MCTS(double[] values) {
        this.values = values;
    }

    double value() {
        return totValue / nVisits;
    }

    // returns avg time in ms.
    double avgTime() {
        return (totalTime / nVisits) * 1000000000 + Math.sqrt(nVisits);
    }

    MCTS getBest() {
        // MCTS best = getBest(this);
        // System.out.println("Best value = " + best.totValue + ", nVisits = " + best.nVisits + ", value = " + best.value());
        return bestNode;
    }

    public static MCTS getBest(MCTS root) {
        if (root.isLeaf()) return root;
        else {
            MCTS left = root.children[0];
            MCTS right = root.children[1];
            if (left.nVisits == 0 && right.nVisits == 0) {
                return root;
            } else if (left.nVisits == 0) {
                return right;
            } else if (right.nVisits == 0) {
                return left;
            } else {
                double leftVal = left.value();
                double rightVal = right.value();
                // When are these values meaningful? only if visited more than once.
                if (leftVal < rightVal) {
                    return getBest(left);
                } else {
                    return getBest(right);
                }
            }
        }
    }

    public void selectAction() {
        List<MCTS> visited = new LinkedList<>();
        MCTS cur = this;
        visited.add(this);
        int frontLeafDepth = 1;
        while (!cur.isLeaf()) {
            cur = cur.select();
            visited.add(cur);
            frontLeafDepth++;
        }
        cur.expand(frontLeafDepth + 1);
        MCTS newNode = cur.select();
        visited.add(newNode);
        double value = rollOut(newNode);
        for (MCTS node : visited) {
            // would need extra logic for n-player game
            node.updateStats(value);
        }
    }

    public void expand(int currentDepth) {
        children = new MCTS[2];
        double[] leftArr = new double[values.length];
        double[] rightArr = new double[values.length];
        System.arraycopy(values, 0, leftArr, 0, values.length);
        System.arraycopy(values, 0, rightArr, 0, values.length);

        int valueIndex = (currentDepth - 2) % values.length; // So that I open the first value first.
        int currentLayer = (currentDepth - 2) / values.length;
        double delta = 1.0 / (1 << (currentLayer + 2));

        leftArr[valueIndex] -= delta;
        rightArr[valueIndex] += delta;

        children[0] = new MCTS(leftArr);
        children[1] = new MCTS(rightArr);

    }

    private MCTS select() {
        MCTS selected = null;
        double bestValue = Double.MIN_VALUE;
        for (MCTS c : children) {
            double uctValue = c.totValue / (c.nVisits + epsilon) +
                    Diversity * Math.sqrt(Math.log(nVisits + 1) / (c.nVisits + epsilon)) +
                    r.nextDouble() * epsilon;
            // small random number to break ties randomly in unexpanded nodes
            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        return selected;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public static double lossFunc(double x) {
        return Math.pow(1.0 / 1000, x);
    }

    public double rollOut(MCTS leaf) {
        // ultimately a roll out will end in some value
        // assume for now that it ends in a win or a loss
        // and just return this at random
        int[] ints = {60};

        long before = System.nanoTime();
        GeneticStringMain$.MODULE$.alg(new Params(ints, leaf.values), 0.3).run(0);
        long after = System.nanoTime();
        double realTime = after - before;
        System.out.println("Time = " + String.format("%03.2f", realTime / 1000000)+ ",\t\t Params = " + Arrays.toString(leaf.values));
        double time = Math.min(Math.max(realTime, 0), 1);
        double loss = lossFunc(time);
        leaf.totalTime += time;
        // System.out.println("RealTime = " + realTime + ", Time = " + time + ", Loss = " + loss);
        return loss;
        // 1 - best, 0 - worst
    }

    public void updateStats(double value) {
        nVisits++;
        totValue += value;
        if (value() > bestNode.value()) {
            bestNode = this;
        }
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }

    public static void main(String[] args) {
        MCTS mcts = initMCTS(4);

        double Time = 100.0;
        long startTime = System.currentTimeMillis();
        long i = 0;
        while (true) {
            System.out.print("Round " + i + ": ");
            if(i%100 == 0){
                MCTS best = mcts.getBest();
                System.out.println("BEST: time = " + String.format("%.1f ms", best.avgTime()) + "; value = " + String.format("%.3f", best.value()) + "; " + Arrays.toString(best.values));
            }


            mcts.selectAction();
            i++;
        }
    }
}
