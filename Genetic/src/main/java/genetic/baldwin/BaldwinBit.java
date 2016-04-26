package genetic.baldwin;

import java.util.Random;

public class BaldwinBit {
    public static final byte Zero = 0;
    public static final byte One = 1;
    public static final byte QuestionMark = 2;

    public static byte genBit(Random rand) {
        // XorShift's native method is nextLong
        return (byte) (rand.nextLong() & 1);
    }

    public static byte genBaldwinBit(Random rand) {
        if (rand.nextBoolean()) {
            return QuestionMark; // Question Mark
        } else {
            return genBit(rand);
        }
    }

}
