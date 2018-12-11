/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minhash;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ruioc
 */
public class MinHashTest {
    
    public MinHashTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of generateSignature method, of class MinHash.
     */
    @Test
    public void testGenerateSignature() {
        System.out.println("generateSignature");
        Set<Integer> shingleSet = null;
        Map<String, List<Integer>> coefMap = null;
        MinHash instance = null;
        LinkedList<Integer> expResult = null;
        LinkedList<Integer> result = instance.generateSignature(shingleSet, coefMap);
        assertEquals(expResult, result);
    }

    /**
     * Test of generateHashCoef method, of class MinHash.
     */
    @Test
    public void testGenerateHashCoef() {
        System.out.println("generateHashCoef");
        MinHash instance = null;
        Map<String, List<Integer>> expResult = null;
        Map<String, List<Integer>> result = instance.generateHashCoef();
        assertEquals(expResult, result);
    }

    /**
     * Test of generateOddCoeff method, of class MinHash.
     */
    @Test
    public void testGenerateOddCoeff() {
        System.out.println("generateOddCoeff");
        MinHash instance = null;
        int expResult = 0;
        int result = instance.generateOddCoeff();
        assertEquals(expResult, result);
    }

    /**
     * Test of generateCoeff method, of class MinHash.
     */
    @Test
    public void testGenerateCoeff() {
        System.out.println("generateCoeff");
        MinHash instance = null;
        int expResult = 0;
        int result = instance.generateCoeff();
        assertEquals(expResult, result);
    }

    /**
     * Test of similarity method, of class MinHash.
     */
    @Test
    public void testSimilarity() {
        System.out.println("similarity");
        List<Integer> s1 = null;
        List<Integer> s2 = null;
        MinHash instance = null;
        double expResult = 0.0;
        double result = instance.similarity(s1, s2);
        assertEquals(expResult, result, 0.0);
    }
    
}
