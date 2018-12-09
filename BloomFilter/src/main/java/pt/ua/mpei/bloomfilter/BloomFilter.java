package pt.ua.mpei.bloomfilter;

import java.io.Serializable;
import pt.ua.mpei.hash.FNV1a;
import pt.ua.mpei.hash.MurmurHash;

public class BloomFilter implements Serializable {

    private static final long serialVersionUID = 123L;

    private final BitArray array;
    private final int numberOfHashes;
    private final int bloomSize;

    public BloomFilter(int expectedInserts, double falsePositivesProb) {
        this.bloomSize = calculateSize(expectedInserts, falsePositivesProb);
        this.numberOfHashes = calculateNumberOfHashes(expectedInserts, bloomSize);
        this.array = new BitArray(bloomSize);
    }

    private int calculateSize(int expectedInserts, double falsePositivesProb) {
        return (int) (-expectedInserts * Math.log(falsePositivesProb) / ((Math.log(2) * Math.log(2))));
    }

    private int calculateNumberOfHashes(int expectedInserts, int bloomSize) {
        return Math.max(1, (int) Math.round(bloomSize / expectedInserts * Math.log(2)));
    }

    public final boolean insert(byte[] bytes) {
        int firstHash = (int) MurmurHash.hash64(bytes, bytes.length);
        int secondHash = (int) FNV1a.hash64(bytes, bytes.length);

        boolean bitChange = false;
        for (int i = 1; i <= this.numberOfHashes; i++) {
            int nextHash = firstHash + i * secondHash;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            bitChange |= this.array.set(nextHash % this.array.getSize());
        }
        return bitChange;
    }

    public final boolean isMember(byte[] bytes) {
        int firstHash = (int) MurmurHash.hash64(bytes, bytes.length);
        int secondHash = (int) FNV1a.hash64(bytes, bytes.length);

        for (int i = 1; i <= this.numberOfHashes; i++) {
            int nextHash = firstHash + i * secondHash;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            if (!this.array.get(nextHash % this.array.size)) {
                return false;
            }
        }
        return true;
    }
}
