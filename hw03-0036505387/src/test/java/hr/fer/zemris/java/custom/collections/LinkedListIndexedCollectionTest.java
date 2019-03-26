package hr.fer.zemris.java.custom.collections;

import org.junit.jupiter.api.BeforeEach;

class LinkedListIndexedCollectionTest extends CollectionTest {

	/**
	 * Actual tests are in {@link CollectionTest}.
	 *
	 * The test methods are inherited from it, so this file
	 * tests the demo codes with {@link LinkedListIndexedCollection}.
	 *
	 * This file should be run to check tests.
	 */
	@Override
	@BeforeEach
	void beforeEach() {
		col = new LinkedListIndexedCollection();
		col1 = new LinkedListIndexedCollection();
		col2 = new LinkedListIndexedCollection();
	}

}
