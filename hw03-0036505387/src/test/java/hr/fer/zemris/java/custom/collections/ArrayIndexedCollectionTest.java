package hr.fer.zemris.java.custom.collections;

import org.junit.jupiter.api.BeforeEach;

class ArrayIndexedCollectionTest extends CollectionTest {

	/**
	 * Actual tests are in {@link CollectionTest}.
	 *
	 * The test methods are inherited from it, so this file
	 * tests the demo codes with {@link ArrayIndexedCollection}.
	 *
	 * This file should be run to check tests.
	 */
	@Override
	@BeforeEach
	void beforeEach() {
		col = new ArrayIndexedCollection();
		col1 = new ArrayIndexedCollection();
		col2 = new ArrayIndexedCollection();
	}

}
