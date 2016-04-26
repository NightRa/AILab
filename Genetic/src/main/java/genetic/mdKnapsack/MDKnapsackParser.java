package genetic.mdKnapsack;

import java.util.Scanner;

public class MDKnapsackParser {
    public static MDKnapsackInstance parse(String data) {
        Scanner in = new Scanner(data);
        int numSacks = in.nextInt();
        int numItems = in.nextInt();
        int[] values = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            values[i] = in.nextInt();
        }
        int[] capacities = new int[numSacks];
        for (int i = 0; i < numSacks; i++) {
            capacities[i] = in.nextInt();
        }

        Sack[] sacks = new Sack[numSacks];
        for (int i = 0; i < numSacks; i++) {
            int[] weights = new int[numItems];
            for (int j = 0; j < numItems; j++) {
                weights[j] = in.nextInt();
            }
            sacks[i] = new Sack(capacities[i], weights);
        }

        int optimum = in.nextInt();

        in.skip("(\r|\n| |\t)*");

        String name = in.nextLine();

        return new MDKnapsackInstance(name, values, sacks, optimum);
    }

}
