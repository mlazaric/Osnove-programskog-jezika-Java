package hr.fer.zemris.java.hw07.demo2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrimesCollectionTest {

    @Test
    void testNegativeNumberOfPrimes() {
        assertThrows(IllegalArgumentException.class, () -> new PrimesCollection(-5));
    }

    @Test
    void testZeroNumberOfPrimes() {
        assertThrows(IllegalArgumentException.class, () -> new PrimesCollection(0));
    }

    @Test
    void testDemo1() {
        List<Integer> actual = new ArrayList<>();
        PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them

        for(Integer prime : primesCollection) {
            actual.add(prime); // Instead of printing, add them to a list so we can easily test if it matches the expected output.
        }

        List<Integer> expected = Arrays.asList(2, 3, 5, 7, 11);

        assertEquals(expected, actual);
    }

    @Test
    void testDemo2() {
        List<Integer> actual1 = new ArrayList<>();
        List<Integer> actual2 = new ArrayList<>();
        PrimesCollection primesCollection = new PrimesCollection(2);

        for(Integer prime : primesCollection) {
            for(Integer prime2 : primesCollection) {
                actual1.add(prime);
                actual2.add(prime2);
                // Instead of printing, add them to a list so we can easily test if it matches the expected output.
            }
        }

        List<Integer> expected1 = Arrays.asList(2, 2, 3, 3);
        List<Integer> expected2 = Arrays.asList(2, 3, 2, 3);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

}