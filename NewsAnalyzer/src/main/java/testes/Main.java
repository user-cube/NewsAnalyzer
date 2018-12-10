/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testes;

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

public class Main {

    private static final String LETTERS
            = "abcdefghijklmnopqrstuvexyABCDEFGHIJKLMNOPQRSTUVWYXZzéèêàôû";

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

        //------------------BLOOMFILTER------------------//
        //Size 1050 - Acceptable false positive rate
        BloomFilter bloom = new BloomFilter(1000, 0.1);

        //Insert 1000 elements
        int count = 0;
        Scanner sc = new Scanner(new File("dataset/articles_1000.train"));
        while (sc.hasNextLine()) {
            bloom.insert(sc.nextLine().getBytes());
            count++;
        }
        System.out.println("Number of inserted elements: " + count);

        //-----------------TEST FALSE POSITIVES----------------//
        //Lets generate 10000 random strings
        Random rand = new SecureRandom();
        int fp = 0;
        for (int i = 0; i < 1000; i++) {
            String randString = Main.randomString(rand);
            if (bloom.isMember(randString.getBytes())) {
                fp++;
            }
        }
        System.out.println("Number of false positives : " + fp);
        System.out.println("Percentage of fp :" + (float) fp / 1000);

        // Save the bloom if we want to use it later
        FileOutputStream fos = new FileOutputStream("mpei.bloom");
        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(bloom);
        }

        // Load previously saved Bloom
        FileInputStream fis = new FileInputStream("mpei.bloom");
        try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            BloomFilter result;
            result = (BloomFilter) ois.readObject();
        }

        //-------------------------MINHASH------------------------------//
        //Each has function is generated using the formula 
        // (ax+b) mod p (Universal Hashing)
        // x - the key to hash
        // a - odd number between 1 to p-1 (Coefficient)
        // b - number between 0 to p-1      (Coefficient)
        // P - prime number greater than the possible max value of x
        // default p is 214748383
        List<Document> docList = new ArrayList<>();

        //Parse training articles for whom we know the true result (articles_1000.true)
        //Each line is an article with the format {ID} {text}
        //Each line is a document which is identified by the ID
        //Parse the file and k-shingle each text with k = 5
        sc = new Scanner(new File("dataset/articles_1000.train"));
        while (sc.hasNextLine()) {
            Document doc = new Document(sc.next()); //Create a new document with the article id
            Set<Integer> shingle = Shingles.shingling(sc.nextLine(), 5); //Shingle the article
            doc.setShingle(shingle);
            docList.add(doc);
        }

        //init minhash with 200 hash functions (should be enough?!)  and generate the hash coefficients
        MinHash minHash = new MinHash(200);

        //random coefmap (a & b)
        Map<String, List<Integer>> coefMap = minHash.generateHashCoef();

        //For each document we have, we generate a signature
        docList.forEach((d) -> {
            d.setSignature(minHash.generateSignature(d.getShingleSet(), coefMap));
        });

        //Create a truth map (Known pairs)
        Map<String, String> truthMap = new HashMap<>();
        sc = new Scanner(new File("dataset/articles_1000.truth"));
        while (sc.hasNextLine()) {
            String[] pair = sc.nextLine().split(" ");
            truthMap.put(pair[0], pair[1]);
        }

        //Find all pair of documents with a similarity index > 0.5
        Set<Set<String>> similaritySet;
        similaritySet = new HashSet<>();
        int trueResults = 0;
        int falseResults = 0;
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
    }
    
    /**
     * Generates a random String.
     * @param r
     * @return String(ab)
     *          - Random generated String.
     */
    private static String randomString(Random r) {
        int wordLen;
        do {
            wordLen = 5 + 2 * (int) (r.nextGaussian() + 0.5d);
        } while (wordLen < 1 || wordLen > 12);
        StringBuilder sb = new StringBuilder(wordLen);
        for (int i = 0; i < wordLen; i++) {
            char ch = LETTERS.charAt(r.nextInt(LETTERS.length()));
            sb.append(ch);
        }
        return new String(sb);
    }
}
