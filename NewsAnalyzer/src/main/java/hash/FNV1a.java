/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hash;

/**
 * FNV1a 64 bit variant.
 * 64 bit Java port of http://www.isthe.com/chongo/src/fnv/hash_64a.c
 */
public class FNV1a {
  private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
  private static final long FNV1_PRIME_64 = 1099511628211L;

  /**
   * FNV1a 64 bit variant.
   *
   * @param data - input byte array
   * @return - hashcode
   */
  public static long hash64(byte[] data) {
    return hash64(data, data.length);
  }

  /**
   * FNV1a 64 bit variant.
   *
   * @param data   - input byte array
   * @param length - length of array
   * @return - hashcode
   */
  public static long hash64(byte[] data, int length) {
    long hash = FNV1_64_INIT;
    for (int i = 0; i < length; i++) {
      hash ^= (data[i] & 0xff);
      hash *= FNV1_PRIME_64;
    }

    return hash;
  }
}
