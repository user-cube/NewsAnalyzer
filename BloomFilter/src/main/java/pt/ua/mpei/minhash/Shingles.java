/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.mpei.minhash;

/**
 * Imports section.
 */
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Métodos Probabilísticos Para a Engenharia Informática.
 * 2018-2019.
 * Prática 4.
 * Professor: António Teixeira.
 * Alunos: Rui Coelho e Vitor Fajardo.
 */


public class Shingles {

    /**
     * Returns a set of k-shingles from a text.
     * 
     * Shingle is hascoded using java's hashcode();
     * 
     * The shingle size is given by:
     *     size = text length - (k - 1)
     * 
     * @param line
     *          Line content.
     * @param k
     *          K Shingle.
     * @return 
     *      - Returns a set of k-shingles from text.
     */
    public static Set<Integer> shingling(String line, int k) {
        String[] text;
        StringBuilder stringBuilder;
        Set<Integer> shingles;
        
        text = line.trim().replaceAll("[^\\p{L}0-9 ]", "").toLowerCase().split("\\s+");
        shingles = new LinkedHashSet<>();
        
        for (int i = 0; i < text.length - k + 1; i++) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(text[i]);
            for (int j = 1; j < k; j++) {
                stringBuilder.append(" ").append(text[i + j]);
            }
            shingles.add(stringBuilder.toString().hashCode());
        }
        return shingles;
    }
    
}
