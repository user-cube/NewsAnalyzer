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
import java.util.BitSet;

/**
 * Generic implementation of an Array of Bits.
 * 
 * Métodos Probabilísticos Para a Engenharia Informática.
 * 2018-2019.
 * Prática 4.
 * Professor: António Teixeira.
 * Alunos: Rui Coelho e Vitor Fajardo.
 */

public class BitArray implements Serializable {
    
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 144L;

    /**
     * Array declaration.
     */
    final BitSet array;
    
    /**
     * Size declaration.
     */
    final int size;
    
    /**
     * Bit Array instanciation.
     * 
     * @param nbits
     *          - Number of bits.
     */
    public BitArray(int nbits) {
        array = new BitSet(nbits);
        size = nbits;
    }
    
    /**
     * Sizer Getter.
     * 
     * @return size
     *          - Size.
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Set array index.
     * 
     * @param index
     *             Array index.
     * @return true
     */    
    public boolean set(int index) {
        array.set(index);
        return true;
    }
    /**
     * Get array index.
     * 
     * @param index
     *          Array index.
     * 
     * @return array.get(index)
     *           - Getted index.
     *          
     */
    public boolean get(int index) {
        return array.get(index);
    }
}
