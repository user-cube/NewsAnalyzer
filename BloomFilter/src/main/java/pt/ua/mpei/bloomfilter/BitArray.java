package pt.ua.mpei.bloomfilter;

import java.io.Serializable;
import java.util.BitSet;

public class BitArray implements Serializable {

    private static final long serialVersionUID = 144L;

    final BitSet array;
    final int size;

    public BitArray(int nbits) {
        array = new BitSet(nbits);
        size = nbits;
    }

    public int getSize() {
        return size;
    }

    public boolean set(int index) {
        array.set(index);
        return true;
    }

    public boolean get(int index) {
        return array.get(index);
    }
}
