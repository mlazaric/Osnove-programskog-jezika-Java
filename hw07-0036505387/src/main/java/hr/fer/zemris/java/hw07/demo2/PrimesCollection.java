package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PrimesCollection implements Iterable<Integer> {

    public static final int MIN_NUMBER_OF_PRIMES_TO_GENERATE = 1;
    private final int numberOfPrimesToGenerate;

    public PrimesCollection(int numberOfPrimesToGenerate) {
        if (numberOfPrimesToGenerate < MIN_NUMBER_OF_PRIMES_TO_GENERATE) {
            throw new IllegalArgumentException("Number of primes must be greater than 0.");
        }

        this.numberOfPrimesToGenerate = numberOfPrimesToGenerate;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new PrimeIterator();
    }

    private class PrimeIterator implements Iterator<Integer> {

        private int primesGenerated = 0;
        private int currentPrime = 1; // next will increment it to 2

        @Override
        public boolean hasNext() {
            return primesGenerated < numberOfPrimesToGenerate;
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Prime iterator is exhausted.");
            }

            ++primesGenerated;

            do {
                ++currentPrime;
            } while(!isPrime(currentPrime));

            return currentPrime;
        }

        private boolean isPrime(int number) {
            double sqrt = Math.sqrt(number);

            // If it is an even number, it is definitely not a prime.
            if (number % 2 == 0 && number > 2) {
                return false;
            }

            // Since we ruled out 2 as a factor, we can check only odd factors.
            for (int factor = 3; factor <= sqrt; factor += 2) {
                if (number % factor == 0) {
                    return false;
                }
            }

            return true;
        }
    }
}
