package mdKnapsack;

import util.BitSet;

import java.util.Random;

public class MDKnapsack {

    // TODO: Binary AND
    public static BitSet mate(BitSet x, BitSet y, MDKnapsackInstance instance, Random rand) {
        int size = instance.values().length;
        int index = rand.nextInt(size);
        BitSet offspring = x.clone();
        for (int i = index; i < size; i++) {
            if(y.get(i))
                offspring.set(i);
            else
                offspring.clear(i);
        }
        return offspring;
    }

}
