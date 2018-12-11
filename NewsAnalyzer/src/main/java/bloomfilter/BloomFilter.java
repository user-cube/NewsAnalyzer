/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bloomfilter;

/**
 * Imports section.
 */
import java.io.Serializable;
import hash.FNV1a;
import hash.MurmurHash;

/**
 * Métodos Probabilísticos Para a Engenharia Informática.
 * 2018-2019.
 * Prática 4.
 * Professor: António Teixeira.
 * Alunos: Rui Coelho e Vitor Fajardo.
 */

public class BloomFilter implements Serializable {
    
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 123L;
    
    /**
     * Declaration - Array of bits.
     */
    private final BitArray array;
    /**
     * Number of hashes.
     */
    private final int numberOfHashes;
    /**
     * BloomFilter size.
     */
    private final int bloomSize;

    /**
     * Constructor.
     * 
     * @param expectedInserts
     *          Number of expected Inserts.
     * 
     * @param falsePositivesProb 
     *          Probability of false positives.
     */
    public BloomFilter(int expectedInserts, double falsePositivesProb) {
        this.bloomSize = calculateSize(expectedInserts, falsePositivesProb);
        this.numberOfHashes = calculateNumberOfHashes(expectedInserts, bloomSize);
        this.array = new BitArray(bloomSize);
    }
    
    /**
     * Calculates de BloomFilter sizes.
     * 
     * @param expectedInserts
     *          Number of expected inserts.
     * 
     * @param falsePositivesProb
     *          Probability of False Positives.
     * 
     * @return size
     *          - The bloomfilter size.
     */
    private int calculateSize(int expectedInserts, double falsePositivesProb) {
        int size;
        size = (int) (-expectedInserts * Math.log(falsePositivesProb) / ((Math.log(2) * Math.log(2))));
        return size;
    }
    
    /**
     * Calculates the number of hashes.
     * 
     * @param expectedInserts
     *          Expected number of inserts.
     * 
     * @param bloomSize
     * ´        BloomFilter size.
     * 
     * @return numHashes
     *          - The number of hashes.
     * 
     */
    private int calculateNumberOfHashes(int expectedInserts, int bloomSize) {
        int numHashes;
        numHashes = Math.max(1, (int) Math.round(bloomSize / expectedInserts * Math.log(2)));
        return numHashes;
    }
    
    /**
     * Insertion method.
     * 
     * @param bytes
     *          Array of bytes.
     * 
     * @return bitChanges
     *          - The bit changes.
     */
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
    
    /**
     * Check if is member.
     * 
     * @param bytes
     *          Array of bytes.
     * 
     * @return True if member, otherwise False.
     */
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
