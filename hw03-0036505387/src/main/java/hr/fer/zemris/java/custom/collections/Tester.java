package hr.fer.zemris.java.custom.collections;

/**
 * Implements an abstract tester.
 *
 * @author Marko Lazarić
 *
 */
public interface Tester {

	/**
	 * Tests the passed object and returns the result.
	 *
	 * @param obj the object to test
	 * @return true if {@code obj} has passed the test,
	 *         false otherwise
	 */
	boolean test(Object obj);

}
