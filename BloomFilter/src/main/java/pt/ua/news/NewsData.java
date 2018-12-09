/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.news;

/**
 * Imports section.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.mpei.minhash.MinHash;

/**
 * Métodos Probabilísticos Para a Engenharia Informática. 
 * 2018-2019. 
 * Prática 4.
 * Professor: António Teixeira. 
 * Alunos: Rui Coelho e Vitor Fajardo.
 */
public class NewsData {

    /**
     * Dataset Structure: 1) First Line - Review Title 2) Remainder - The review
     * content
     *
     * The files are organized by categories each category have it's own folder
     * which will later be used to separate the contents of the graphical
     * interface created with Java Swing.
     */
    
    
    /**
     * Reviews Map.
     */
    private Map<String, List<Review>> map = new HashMap<>();
    /**
     * Array of reviews.
     */
    private Review[] lista;
    /**
     * MinHash instanciation.
     */
    private MinHash minHash;
    /**
     * Coefficient Map.
     */
    private Map<String, List<Integer>> coefMap;

    /**
     * Constructor. Reads the dataset and map it in: 1. By ID. 2. By category
     * (Map).
     *
     *
     * @param path Dataset path
     */
    public NewsData(String path) {
        Scanner sc;
        int cont = 0;
        File folder = new File(path);
        for (File subFolder : folder.listFiles()) {
            String category = subFolder.getName();
            map.put(category, new ArrayList<>());
            for (File article : subFolder.listFiles()) {
                try {
                    sc = new Scanner(article);
                    String title = sc.nextLine();
                    StringBuilder content = new StringBuilder();
                    while (sc.hasNext()) {
                        content.append(sc.nextLine());
                    }
                    map.get(category).add(new Review(title, content.toString()));
                    cont++;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NewsData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            lista = new Review[cont];
            map.values().forEach((l) -> {
                l.forEach((a) -> {
                    lista[a.getId()] = a;
                });
            });
        }
        //after all the files are loaded initialize the minHash
        initMinHash();
    }

    /**
     * Search function. BloomFilter is used to check if the words introduced by
     * the user are in the reviews.
     *
     * @param category Review category.
     *
     * @param input Text introduced by the user.
     *
     * @return Reviews if the text is in at least one of the reviews, if not
     * there is no reviews to present, empty list.
     */
    public List<Review> search(String category, String input) {
        List<Review> out = new ArrayList<>();
        if (category.equals("any")) {
            for (Review a : lista) {
                if (a.search(input.split("[ .,;-]")) > 0) {
                    out.add(a);
                }
            }
        } else {
            map.get(category).stream().filter((a) -> (a.search(input.split("[ .,;-]")) > 0)).forEachOrdered((a) -> {
                out.add(a);
            });
        }
        return out;
    }

    /**
     * Initializes MinHash.
     *
     * Generate: 1. The Coefficient Map 2. Signatures to all reviews.
     */
    public final void initMinHash() {
        minHash = new MinHash(200);
        coefMap = minHash.generateHashCoef();

        for (Review a : lista) {
            a.setSignature(minHash.generateSignature(a.getShingleSet(), coefMap));
        }

    }

    /**
     * Analysis and comparison of all reviews.
     *
     *
     * If similarity is bigger then 0 returns that reviews as result.
     *
     * @param a
     * @return - Similar reviews. (similarity > 0)
     */
    public List<Review> compare(Review a) {
        Set<Set<String>> similaritySet;
        similaritySet = new HashSet<>();
        List<Review> out = new ArrayList<>();
        for (Review a2 : lista) {
            if (a.getId() != a2.getId()) {
                double simi = minHash.similarity(a.getSignature(), a2.getSignature());
                if (simi > 0) {
                    out.add(a2);
                }
            }
        }
        return out;
    }
}
