/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.mpei.minhash;

/**
 * Import section.
 */
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Métodos Probabilísticos Para a Engenharia Informática.
 * 2018-2019.
 * Prática 4.
 * Professor: António Teixeira.
 * Alunos: Rui Coelho e Vitor Fajardo.
 */

public class Document {
    /**
     * Review identification.
     */
    private final String identification;
    /**
     * Shingle Set.
     */
    private Set<Integer> shingleSet;
    /**
     * List of signatures.
     */
    private LinkedList<Integer> signature;

    /**
     * Constructor.
     * 
     * @param identification
     *          - Identification.
     */
    public Document(String identification) {
        this.identification = identification;
        this.shingleSet = new LinkedHashSet<>();
    }

    /**
     * Identification Getter.
     * 
     * @return identification
     *          - Identification.
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * Siganture Setter.
     * 
     * @param signature
     *         - Signatures.
     * 
     */
    public void setSignature(LinkedList<Integer> signature) {
        this.signature = signature;
    }
    
    /**
     * Shigle Setter.
     * 
     * @param shingleSet
     *          - Shigle Set.
     */
    public void setShingle(Set<Integer> shingleSet) {
        this.shingleSet = shingleSet;
    }
    
    /**
     *  Signature Getter.
     * 
     * @return signature
     *          - Signature.
     */
    public LinkedList<Integer> getSignature() {
        return signature;
    }

    /**
     * Shigle Set Getter.
     * 
     * @return shingleset
     *      - The Shigle Set
     * 
     */
    public Set<Integer> getShingleSet() {
        return shingleSet;
    }
    
    
}
