package genetic.knapsack;

import java.util.Random;

public final class KnapsackMutate {
    public static void binomialMutate(double mutateProb, KnapsackElement instance, Random rand) {
        for (int i = 0; i < instance.amounts().length; i++)
            if (rand.nextDouble() < mutateProb)
                instance.amounts()[i]++;
        instance.trim(rand);
    }

    public static void onePointMutate(KnapsackElement instance, Random rand) {
        int index = rand.nextInt(instance.amounts().length);
        instance.amounts()[index]++;
        instance.trim(rand);
    }
}
