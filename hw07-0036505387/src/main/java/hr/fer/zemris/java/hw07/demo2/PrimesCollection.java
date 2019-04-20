package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;

public class PrimesCollection implements Iterable<Integer> {

    private final int numberOfPrimes;

    public PrimesCollection(int numberOfPrimes) {
        if (numberOfPrimes <= 0) {
            throw new IllegalArgumentException("Number of primes must be greater than 0.");
        }

        this.numberOfPrimes = numberOfPrimes;
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
            return primesGenerated < numberOfPrimes;
        }

        @Override
        public Integer next() {
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
