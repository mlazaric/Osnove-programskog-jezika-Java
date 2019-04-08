package hr.fer.zemris.java.custom.collections;

/**
 * Implements an abstract tester.
 *
 * @author Marko Lazarić
 *
 * @param <T> type of object to test
 *
 */
@FunctionalInterface
public interface Tester<T> {

	/**
	 * Tests the passed object and returns the result.
	 *
	 * @param obj the object to test
	 * @return true if {@code obj} has passed the test,
	 *         false otherwise
	 */
	boolean test(T obj);

}
