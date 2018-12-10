/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minhash;

/**
 * Imports section.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Métodos Probabilísticos Para a Engenharia Informática.
 * 2018-2019.
 * Prática 4.
 * Professor: António Teixeira.
 * Alunos: Rui Coelho e Vitor Fajardo.
 */

public class MinHash {
    
    /**
     * Number of Hash Functions.
     */
    private final int numHashFunctions;
    /**
     * Prime number.
     */
    private final int prime = 214748383;

    /**
     * Constructor.
     * 
     * @param numHashFunctions
     *          Number of Hash Functions.
     */
    public MinHash(int numHashFunctions) {
        this.numHashFunctions = numHashFunctions;
    }
    
    /**
     * 
     * @param shingleSet
     *          Shingle set.
     * @param coefMap
     *          The coefficients Map.
     * @return 
     *      Signatures.
     */
    @SuppressWarnings("UnusedAssignment")
    public LinkedList<Integer> generateSignature(Set<Integer> shingleSet, Map<String, List<Integer>> coefMap) {
        
        /**
         * Linked List of signatures.
         */
        LinkedList<Integer> signature;
        /**
         * HashCode.
         */
        Integer hashCode;
        /**
         * Minimum HashCode.
         */
        int minimumHashCode;
        
        /**
         * Intanciation of sigantures.
         */
        signature = new LinkedList<>();
        /**
         * Initialization of hascode.
         */
        hashCode = null;
        int a, b;

        for (int i = 0; i < numHashFunctions; i++) {
            minimumHashCode = Integer.MAX_VALUE;
            a = coefMap.get("a").get(i);
            b = coefMap.get("b").get(i);

            for (Iterator<Integer> it = shingleSet.iterator(); it.hasNext();) {
                Integer shingle = it.next();
                hashCode = Math.abs(((a * shingle + b) % prime));
                if (hashCode < minimumHashCode) {
                    minimumHashCode = hashCode;
                }
            }
            signature.add(minimumHashCode);
        }

        return signature;
    }
    
    /**
     * Generates Hash Coefficients.
     * 
     * @return 
     *      Hash of Coefficients.
     */
    public Map<String, List<Integer>> generateHashCoef() {
        
        /**
         * Declaration of aSet.
         */
        Set<Integer> aSet;
        /**
         * Assignement of aSet.
         */
        aSet = new HashSet<>();
        /**
         * Declaration of bSet.
         */
        Set<Integer> bSet;
        /**
         * Assignement of bSet.
         */
        bSet = new HashSet<>();
        /**
         * Declaration of aList.
         */
        List<Integer> aList;
        /**
         * Assignement of aList.
         */
        aList = new ArrayList<>();
        /**
         * Declaration of bList.
         */
        List<Integer> bList;
        /**
         * Assignement of bList.
         */
        bList = new ArrayList<>();
        
        /**
         * Generate odd coefficients.
         */
        while (aSet.size() < numHashFunctions) {
            aSet.add(generateOddCoeff());
        }
        /**
         * Generate coefficients.
         */
        while (bSet.size() < numHashFunctions) {
            bSet.add(generateCoeff());
        }
        
        /**
         * Declaration of Coefficients Map.
         */
        Map<String, List<Integer>> coefMap;
        /**
         * Assignement of Coefficients Map.
         */
        coefMap = new HashMap();

        aList.addAll(aSet);
        bList.addAll(bSet);
        coefMap.put("a", aList);
        coefMap.put("b", bList);

        return coefMap;
    }
    
    /**
     * Generates the odd coefficients.
     * @return odd
     *        - Odd coefficient.
     */
    public int generateOddCoeff() {
        int odd;
        odd = 1 + 2 * (int) (Math.random() * ((prime - 1) / 2 + 1));
        return odd;
    }
    
    /**
     * Generate coefficients.
     * @return coeff
     *         - Coefficient.
     */
    public int generateCoeff() {
        int coeff;
        coeff = 1 + (int) (Math.random() * ((prime - 1) + 1));
        return coeff;
    }
    
    /**
     * Function that compares similarity.
     * 
     * @param s1
     *         List of signatures.
     * @param s2
     *         List of signatures.
     * @return similarity
     *         Returns the similarity value.
     */
    public double similarity(List<Integer> s1, List<Integer> s2) {

        if (s1.size() != s2.size()) {
            throw new IllegalArgumentException("Signatures need to have the same size");
        }
        int count = 0;
        for (int i = 0; i < s1.size(); i++) {
            if (s1.get(i).equals(s2.get(i))) {
                count++;
            }
        }
        float similarity;
        similarity = (float) count / numHashFunctions;
        return similarity;
    }

}
