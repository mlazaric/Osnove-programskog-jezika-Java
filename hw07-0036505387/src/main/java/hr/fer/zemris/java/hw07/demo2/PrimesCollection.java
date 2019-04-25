package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Models a simple collection which contains a certain number of prime numbers.
 *
 * @author Marko Lazarić
 */
public class PrimesCollection implements Iterable<Integer> {

    /**
     * The minimum number of primes to generate.
     */
    public static final int MIN_NUMBER_OF_PRIMES_TO_GENERATE = 1;

    /**
     * The number of primes to generate.
     */
    private final int numberOfPrimesToGenerate;

    /**
     * Creates a new {@link PrimesCollection} with the given arguments.
     *
     * @param numberOfPrimesToGenerate the number of primes to generate
     *
     * @throws IllegalArgumentException if {@code numberOfPrimesToGenerate} is less than {@code #MIN_NUMBER_OF_PRIMES_TO_GENERATE}
     */
    public PrimesCollection(int numberOfPrimesToGenerate) {
        if (numberOfPrimesToGenerate < MIN_NUMBER_OF_PRIMES_TO_GENERATE) {
            throw new IllegalArgumentException("Number of primes must be greater than 0.");
        }

        this.numberOfPrimesToGenerate = numberOfPrimesToGenerate;
    }

    /**
     * Iterates over the "prime collection". Each iteration generates the next prime.
     * Exactly {@link #numberOfPrimesToGenerate} primes are generated.
     *
     * @return an iterator over the "prime collection"
     */
    @Override
    public Iterator<Integer> iterator() {
        return new PrimeIterator();
    }

    /**
     * A prime number generator that generates exactly {@link #numberOfPrimesToGenerate} primes before stopping.
     *
     * @author Marko Lazarić
     */
    private class PrimeIterator implements Iterator<Integer> {

        /**
         * The number of primes it has generated so far.
         */
        private int primesGenerated = 0;

        /**
         * The last generated prime number.
         */
        private int currentPrime = 1; // next will increment it to 2

        /**
         * Returns true if it still has not yet generated {@link #numberOfPrimesToGenerate} prime numbers,
         * false otherwise.
         *
         * @return true if it still has not yet generated {@link #numberOfPrimesToGenerate} prime numbers,
         *         false otherwise
         */
        @Override
        public boolean hasNext() {
            return primesGenerated < numberOfPrimesToGenerate;
        }

        /**
         * Returns the next generated prime number.
         *
         * @return the next generated prime number
         *
         * @throws NoSuchElementException if it has already generated {@link #numberOfPrimesToGenerate} prime numbers
         */
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

        /**
         * Returns true if the number is a prime, false otherwise.
         * It should not be called for negative numbers.
         *
         * @param number the number to check for primeness
         * @return true if the number is a prime,
         *         false otherwise
         */
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
