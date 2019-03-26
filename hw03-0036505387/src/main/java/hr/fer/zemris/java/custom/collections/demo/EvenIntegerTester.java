package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Tester;

/**
 * A simple tester that only accepts even integers.
 *
 * @author Marko Lazarić
 *
 */
public class EvenIntegerTester implements Tester { //Needs to be public for CollectionTest.

	/**
	 * Returns true if {@code obj} is an even integer,
	 * otherwise it returns false.
	 */
	@Override
	public boolean test(Object obj) {
		if(!(obj instanceof Integer)) {
			return false;
		}

		Integer i = (Integer) obj;

		return i % 2 == 0;
	}

}