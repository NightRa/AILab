package mdKnapsack;

import util.BitSet;
import util.IntBuffer;

import java.util.Random;

public class MDKnapsack {

    public static BitSet mate(BitSet x, BitSet y, MDKnapsackInstance instance, Random rand) {
        int size = x.numBits;
        int index = rand.nextInt(size);
        return BitSet.crossOver(x, y, index);
    }

    // @Requires a proof of ownership for the IntBuffer
    public static void takenItems(BitSet items, IntBuffer takenItemsBuffer) {
        takenItemsBuffer.clear();
        BitSet cloned_items = items.clone();
        int i = cloned_items.lowestBit();
        while (i != -1) {
            takenItemsBuffer.add(i);
            cloned_items.clear(i);
            i = cloned_items.lowestBit();
        }
    }

    /**
     * Calculates based on the TakenItems buffer.
     * Requires a proof that the buffer contains the correct values.
     * Can be done via @MDKnapsack.takenItems
     **/
    public static int weightOfItems(IntBuffer takenItems, int[] itemWeights) {
        int sum = 0;
        for (int i : takenItems) {
            sum += itemWeights[i];
        }
        return sum;
    }

    /**
     * Calculates based on the TakenItems buffer.
     * Requires a proof that the buffer contains the correct values.
     * Can be done via @MDKnapsack.takenItems
     **/
    public static int valueOfItems(IntBuffer takenItems, int[] values) {
        int sum = 0;
        for (int i : takenItems) {
            sum += values[i];
        }
        return sum;
    }

    // @Requires a proof of ownership for the IntBuffer
    public static void trim(BitSet items, Sack[] sacks, IntBuffer takenItemsBuffer, Random rand) {
        // Trim to each sack at a time.
        for (Sack sack : sacks) {
            // Inside, because it changes, and weightOfItems requires the correct one.
            // @Requires a proof of ownership for the IntBuffer
            takenItems(items, takenItemsBuffer);

            int[] itemWeights = sack.itemWeights();
            int weightInSack = weightOfItems(takenItemsBuffer, itemWeights); // Contains the correct values, because we just calculated them w/ takenItems.
            int sackCapacity = sack.capacity();
            while (weightInSack > sackCapacity) {
                int dropIndex = takenItemsBuffer.get(rand.nextInt(takenItemsBuffer.size()));
                if (items.get(dropIndex)) {
                    items.clear(dropIndex);
                    weightInSack -= itemWeights[dropIndex];
                }
            }
        }
    }

}
