package baldwin;

import java.util.Random;

import static baldwin.BaldwinBit.*;

public class BaldwinBitString {
    public static byte[] randomBaldwinString(int length, Random rand) {
        byte[] arr = new byte[length];
        for (int i = 0; i < length; i++) {
            arr[i] = genBaldwinBit(rand);
        }
        return arr;
    }

    public static int localSearchesTimeRemaining(byte[] baldwinString, byte[] target, int maxIterations, Random rand) {
        for (int iter = 0; iter < maxIterations; iter++) {
            boolean foundTarget = localSearch(baldwinString, target, rand);
            if(foundTarget) return maxIterations - iter;
        }
        return 0;
    }

    public static boolean localSearch(byte[] baldwinString, byte[] target, Random rand) {
        for (int i = 0; i < baldwinString.length; i++) {
            byte currentBit = baldwinString[i];
            if(currentBit == QuestionMark) {
                byte setBit = genBit(rand);
                if(target[i] != setBit) return false;
            } else if (currentBit != target[i]) return false;
        }
        return true;
    }
}
