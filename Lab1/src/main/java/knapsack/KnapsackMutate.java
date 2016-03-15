package knapsack;

import scala.util.Random;

public final class KnapsackMutate {
    public void binomialMutate(KnapsackElement instance, Random rand){
        for (int i = 0; i < instance.amounts().length; i++)
            if (rand.nextBoolean())
                instance.amounts()[i]++;
        instance.trim(rand);
    }

    public void onePointMutate(KnapsackElement instance, Random rand){
        int index = rand.nextInt(instance.amounts().length);
        instance.amounts()[index]++;
        instance.trim(rand);
    }
}
