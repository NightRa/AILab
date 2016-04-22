/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/** A simplified and streamlined version of {@link java.util.BitSet}. */
public final class BitSet implements Serializable, Cloneable {

    private final long[] bits;
    public final int numBits;

    public BitSet(int numBits) {
        this.numBits = numBits;
        int numLongs = numBits >>> 6;
        if ((numBits & 0x3F) != 0) {
            numLongs++;
        }
        bits = new long[numLongs];
    }

    public BitSet(int numBits, long[] bits) {
        this.numBits = numBits;
        this.bits = bits;
    }

    public static BitSet randomBitSet(int numBits, Random rand) {
        int numLongs = numBits >>> 6;
        if ((numBits & 0x3F) != 0) {
            numLongs++;
        }
        long[] bits = new long[numLongs];
        for (int i = 0; i < numLongs; i++) {
            bits[i] = rand.nextLong();
        }
        // Clear all the irrelevant bits in the last block.
        // For canonicity of representation.
        if ((numBits & 0x3F) != 0) {
            bits[numLongs - 1] &= (1 << (numBits & 0x3F)) - 1;
        }
        return new BitSet(numBits, bits);
    }

    public boolean get(int index) {
        // skipping range check for speed
        return (bits[index >>> 6] & 1L << (index & 0x3F)) != 0L;
    }

    public void set(int index) {
        // skipping range check for speed
        bits[index >>> 6] |= 1L << (index & 0x3F);
    }

    public void clear(int index) {
        // skipping range check for speed
        bits[index >>> 6] &= ~(1L << (index & 0x3F));
    }

    public void clear() {
        int length = bits.length;
        for (int i = 0; i < length; i++) {
            bits[i] = 0L;
        }
    }

    @Override
    public BitSet clone() {
        return new BitSet(numBits, bits.clone());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BitSet)) {
            return false;
        }
        BitSet other = (BitSet) o;
        return Arrays.equals(bits, other.bits);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(64 * bits.length);
        int i = 0;
        for (long l : bits) {
            for (int j = 0; j < 64 && i < numBits; j++, i++) {
                result.append((l & 1L << j) == 0 ? '0' : '1');
            }
            result.append(' ');
        }
        return result.toString();
    }

    public int lowestBit() {
        int lowestBit = 0;
        for (int i = 0; i < bits.length; i++) {
            int index = Long.numberOfTrailingZeros(bits[i]);
            lowestBit += index;
            if (index != 64 && lowestBit < numBits) return lowestBit;
        }
        return -1;
    }

    // Crossover operation, @i being the number of bits of @x to be copied. The rest are from @y.
    public static BitSet crossOver(BitSet x, BitSet y, int i) {
        if(x.numBits != y.numBits) {
            throw new IllegalArgumentException("The BitSets must have the same size");
        }
        int crossoverBlock = i >>> 6;
        int crossoverIndex = i & 0x1F;
        long bx = x.bits[crossoverBlock];
        long by = y.bits[crossoverBlock];
        long crossoverMask = (1 << crossoverIndex) - 1;
        long newCrossoverBlock = (crossoverMask & bx) | (~crossoverMask & by);
        long[] newBits = new long[x.bits.length];
        System.arraycopy(x.bits, 0, newBits, 0, crossoverBlock);
        newBits[crossoverBlock] = newCrossoverBlock;
        System.arraycopy(y.bits, crossoverBlock + 1, newBits, crossoverBlock + 1, y.bits.length - (crossoverBlock + 1));
        return new BitSet(x.numBits, newBits);
    }

    public static int hammingDistance(BitSet x, BitSet y) {
        if(x.numBits != y.numBits) {
            throw new IllegalArgumentException("The BitSets must have the same size");
        }
        int distance = 0;
        for (int i = 0; i < x.bits.length; i++) {
            distance += Long.bitCount(x.bits[i] ^ y.bits[i]);
        }
        return distance;
    }
}
