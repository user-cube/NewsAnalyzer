/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testes;

/**
 * Imports section.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import bloomfilter.BloomFilter;
import minhash.Document;
import minhash.MinHash;
import minhash.Shingles;


/**
 * Métodos Probabilísticos Para a Engenharia Informática.
 * 2018-2019.
 * Prática 4.
 * Professor: António Teixeira.
 * Alunos: Rui Coelho e Vitor Fajardo.
 */

public class Main {

    /**
     * Definition of all letters.
     */
    private static final String LETTERS
            = "abcdefghijklmnopqrstuvwyxzç"
            + "ABCDEFGHIJKLMNOPQRSTUVWYXZÇ"
            + "éèêàâáãóòõôúùû"
            + "ÉÈÊÀÂÁÃÓÒÕÔÚÙÛ";
    
    /**
     * Tests intialization.
     * 
     * @param args
     *          Arguments.
     * 
     * @throws FileNotFoundException
     *          If file not found.
     * 
     * @throws IOException
     *          Signals that an I/O exception of some sort has occurred. 
     * @throws ClassNotFoundException
     *          If class not found.
     */
    
    @SuppressWarnings("unused") 
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

        /**
         * Declaration of bloomfilter.
         */
        BloomFilter bloom;
        /**
         * Declaration of scanner.
         */
        Scanner sc;
        /**
         * Declaration of random number.
         */
        Random rand;
        /**
         * Declaration of documents list.
         */
        List<Document> docList;
        /**
         * Declaration of minHash.
         */
        MinHash minHash;
        /**
         * Declaration of Coefficients Map.
         */
        Map<String, List<Integer>> coefMap;
        /**
         * Declaration of True Positives Map.
         */
        Map<String, String> truthMap;
        /**
         * Declaration of Similarity Set.
         */
        Set<Set<String>> similaritySet;
        
        /**
         * Size 1050 - Acceptable false positive rate.
         */
        bloom = new BloomFilter(1000, 0.1);

        /**
         * Insert 1000 elements.
         */
        int count = 0;
        try {
            sc = new Scanner(new File("dataset/articles_1000.train"));
            while (sc.hasNextLine()) {
                bloom.insert(sc.nextLine().getBytes());
                count++;
            }
            System.out.println("Number of inserted elements: " + count);
            
            /**
             * Test false positives.
             * 
             * To start: generate 1000 random strings.
             */
            
            rand = new SecureRandom();
            int fp = 0;
            for (int i = 0; i < 1000; i++) {
                String randString;
                randString = Main.randomString(rand);
                if (bloom.isMember(randString.getBytes())) {
                    fp++;
                }
            }
            
            System.out.println("Number of false positives : " + fp);
            System.out.println("Percentage of fp :" + (float) fp / 1000);
            
        } catch (FileNotFoundException fileNotFoundException) {
            
            System.err.println("File not found!");
            
        }
        
        /**
         * Save the bloom, we'll use it later.
         */
        FileOutputStream fos;
        fos = new FileOutputStream("analise.bloom");
        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(bloom);
        }

        /**
         * Load the previous saved Bloom.
         */
        FileInputStream fis;
        fis = new FileInputStream("analise.bloom");
        
        try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            BloomFilter result;
            result = (BloomFilter) ois.readObject();
        }
        
        /**
         * MinHash.
         * 
         * Universal Hashing - formula:
         * (ax+b) mod p
         * 
         * x - Key to hash
         * a - Odd number between 1 and to p-1 (coefficient)
         * b - Number between 0 to p-1 (coefficient)
         * p - Prime number greater than the possible max value of x
         * Default (p): 214748383
         * 
         * Parse training articles for whom we know the true result (articles_1000.true).
         * Each line is a news with the format {ID} {text}.
         * Each line is a document which is identified by the ID.
         * Parse the file and k-shingle each text with k = 5.
         */
        docList = new ArrayList<>();
        
        try {
            sc = new Scanner(new File("dataset/articles_1000.train"));
            /**
             * Create a new document with the article ID.
             * Shigle the article.
             */
            while (sc.hasNextLine()) {
                Document doc = new Document(sc.next());
                Set<Integer> shingle = Shingles.shingling(sc.nextLine(), 5); 
                doc.setShingle(shingle);
                docList.add(doc);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            
            System.err.println("File not found!");
            
        }
        
        /**
         * Init MinHash with 200 hash functions.
         */
        minHash = new MinHash(200);

        /**
         * Generate Coefficients.
         * (random coefmap (a & b).
         */
        coefMap = minHash.generateHashCoef();
        
        /**
         * Generate a signature for each document.
         */
        docList.forEach((d) -> {
            d.setSignature(minHash.generateSignature(d.getShingleSet(), coefMap));
        });

        /**
         * Creat a truth map (known pairs).
         */
        truthMap = new HashMap<>();
        
        try {
            /**
             * Add the truth articles to the truth map.
             */
            sc = new Scanner(new File("dataset/articles_1000.truth"));
            while (sc.hasNextLine()) {
                String[] pair = sc.nextLine().split(" ");
                truthMap.put(pair[0], pair[1]);
            }

            
            /**
             * Create the similarity HashSet.
             */
            similaritySet = new HashSet<>();
            int trueResults = 0;
            int falseResults = 0;
            
            /**
             * Find all pairs of documents with a similarity index > 0.5.
             */
            for (int i = 0; i < docList.size(); i++) {
                for (int j = i + 1; j < docList.size(); j++) {
                    Document d1 = docList.get(i);
                    Document d2 = docList.get(j);
                    String id1 = d1.getIdentification();
                    String id2 = d2.getIdentification();
                    if (!id1.equals(id2)) {
                        double simi = minHash.similarity(d1.getSignature(), d2.getSignature());
                        if (simi > 0.5) {
                            if (truthMap.containsKey(id1) && truthMap.get(id1).equals(id2)) {
                                trueResults++;
                            } else if (truthMap.containsKey(id2) && truthMap.get(id2).equals(id1)) {
                                trueResults++;
                            } else {
                                falseResults++;
                            }
                        }
                    }
                }
            }
            System.out.println(String.format("True positives %d / %d", trueResults, truthMap.size()));
            System.out.println("False positives : " + falseResults);
            
        } catch (FileNotFoundException fileNotFoundException) {
            
            System.err.println("File not found!");
            
        }
    }
    
    /**
     * Generates a random String.
     * 
     * @param random
     *          Random number.
     * 
     * @return String(sb)
     *          - Random generated String.
     */
    private static String randomString(Random random) {
        int wordLen;
        do {
            wordLen = 5 + 2 * (int) (random.nextGaussian() + 0.5d);
        } while (wordLen < 1 || wordLen > 12);
        StringBuilder sb = new StringBuilder(wordLen);
        for (int i = 0; i < wordLen; i++) {
            char ch = LETTERS.charAt(random.nextInt(LETTERS.length()));
            sb.append(ch);
        }
        return new String(sb);
    }
}
