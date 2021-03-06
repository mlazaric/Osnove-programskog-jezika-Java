package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {

	static ArrayIndexedCollection createCollectionWithNElements(int numberOfElements) {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();

		for (int i = 0; i < numberOfElements; ++i) {
			collection.add(i);
		}

		return collection;
	}

	@Test
	void testIsEmptyWithEmptyCollection() {
		assertTrue(createCollectionWithNElements(0).isEmpty());
	}

	@Test
	void testIsEmptyWithNonEmptyCollection() {
		assertFalse(createCollectionWithNElements(1).isEmpty());
	}

	@Test
	void testSizeWithEmptyCollection() {
		assertEquals(0, createCollectionWithNElements(0).size());
	}

	@Test
	void testSizeWithNonEmptyCollection() {
		assertEquals(5, createCollectionWithNElements(5).size());
	}

	@Test
	void testAddWithNotFullCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		assertEquals(5, collection.size());

		collection.add("Test");

		assertEquals(6, collection.size());
		assertTrue(collection.contains("Test"));
	}

	@Test
	void testAddWithFullCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(16);

		assertEquals(16, collection.size());

		collection.add("Test");

		assertEquals(17, collection.size());
		assertTrue(collection.contains("Test"));
	}

	@Test
	void testAddWithDuplicate() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		assertEquals(5, collection.size());

		collection.add("Test");
		collection.add("Test");

		assertEquals(7, collection.size());
	}

	@Test
	void testAddWithNull() {
		assertThrows(NullPointerException.class, () -> createCollectionWithNElements(0).add(null));
	}

	@Test
	void testAddInsertsAtTheEnd() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		collection.add("Test");
		collection.add("stefica");

		Object[] array = collection.toArray();

		assertEquals("Test", array[array.length - 2]);
		assertEquals("stefica", array[array.length - 1]);
	}

	@Test
	void testContainsWithEmptyCollection() {
		assertFalse(createCollectionWithNElements(0).contains(new Object()));
	}

	@Test
	void testContainsUsesEquals() {
		ArrayIndexedCollection collection = createCollectionWithNElements(1);

		collection.add(new String("Test"));

		assertTrue(collection.contains(new String("Test")));
	}

	@Test
	void testContainsWithNull() {
		assertFalse(createCollectionWithNElements(5).contains(null));
	}

	@Test
	void testRemoveWithEmptyCollection() {
		assertFalse(createCollectionWithNElements(0).remove(new Object()));
	}

	@Test
	void testRemoveUsesEquals() {
		ArrayIndexedCollection collection = createCollectionWithNElements(1);

		collection.add(new String("Test"));

		assertEquals(2, collection.size());
		assertTrue(collection.remove(new String("Test")));
		assertEquals(1, collection.size());
	}

	@Test
	void testRemoveWithNull() {
		assertFalse(createCollectionWithNElements(5).remove(null));
	}

	@Test
	void testRemoveWithNonExistingValue() {
		assertFalse(createCollectionWithNElements(5).remove("stefica"));
	}

	@Test
	void testRemoveRemovesOnlyOne() {
		ArrayIndexedCollection collection = createCollectionWithNElements(1);

		collection.add(new String("Test"));
		collection.add(new String("Test"));

		assertEquals(3, collection.size());
		assertTrue(collection.remove(new String("Test")));
		assertEquals(2, collection.size());
		assertTrue(collection.contains(new String("Test")));
	}

	@Test
	void testToArrayWithEmptyCollection() {
		assertArrayEquals(new Object[] {}, createCollectionWithNElements(0).toArray());
	}

	@Test
	void testToArrayWithNonEmptyCollection() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		Object[] array = new Object[] { "Test", new Object(), Integer.valueOf(5) };

		collection.add(array[0]);
		collection.add(array[1]);
		collection.add(array[2]);

		assertArrayEquals(array, collection.toArray());
	}

	@Test
	void testForEachWithEmptyCollection() {
		createCollectionWithNElements(0).forEach(new Processor() {

			@Override
			public void process(Object value) {
				assertNotNull(value);
			}

		});
	}

	@Test
	void testForEachWithNonEmptyCollection() {
		createCollectionWithNElements(5).forEach(new Processor() {

			@Override
			public void process(Object value) {
				assertNotNull(value);
			}

		});
	}

	@Test
	void testAddAllWithEmptyCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		collection.addAll(createCollectionWithNElements(0));

		assertEquals(5, collection.size());
	}

	@Test
	void testAddAllWithNull() {
		assertThrows(NullPointerException.class, () -> createCollectionWithNElements(5).addAll(null));
	}

	@Test
	void testAddAllWithNonEmptyCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		collection.addAll(createCollectionWithNElements(5));

		assertEquals(10, collection.size());
	}

	@Test
	void testClearWithEmptyCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(0);

		collection.clear();

		assertEquals(0, collection.size());
		assertArrayEquals(new Object[] {}, collection.toArray());
	}

	@Test
	void testClearWithNonEmptyCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		collection.clear();

		assertEquals(0, collection.size());
		assertArrayEquals(new Object[] {}, collection.toArray());
	}

	@Test
	void testInitialCapacityConstructorWithValidCapacity() {
		assertNotNull(new ArrayIndexedCollection(5));
	}

	@Test
	void testInitialCapacityConstructorWithZeroCapacity() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
	}

	@Test
	void testInitialCapacityConstructorWithNegativeCapacity() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(-5));
	}

	@Test
	void testCollectionConstructorWithEmptyCollection() {
		ArrayIndexedCollection oldCollection = createCollectionWithNElements(0);
		ArrayIndexedCollection newCollection = new ArrayIndexedCollection(oldCollection);

		assertEquals(newCollection.size(), oldCollection.size());
		assertArrayEquals(newCollection.toArray(), oldCollection.toArray());
	}

	@Test
	void testCollectionConstructorWithNonEmptyCollection() {
		ArrayIndexedCollection oldCollection = createCollectionWithNElements(5);
		ArrayIndexedCollection newCollection = new ArrayIndexedCollection(oldCollection);

		assertEquals(newCollection.size(), oldCollection.size());
		assertArrayEquals(newCollection.toArray(), oldCollection.toArray());
	}

	@Test
	void testCollectionConstructorWithNull() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
	}

	@Test
	void testTwoArgumentConstructorWithNonEmptyCollectionAndNegativeCapacity() {
		assertThrows(IllegalArgumentException.class,
				() -> new ArrayIndexedCollection(createCollectionWithNElements(5), -5));
	}

	@Test
	void testTwoArgumentConstructorWithEmptyCollectionAndNegativeCapacity() {
		assertThrows(IllegalArgumentException.class,
				() -> new ArrayIndexedCollection(createCollectionWithNElements(0), -5));
	}

	@Test
	void testTwoArgumentConstructorWithNullCollection() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 5));
	}

	@Test
	void testGetWithNegativeIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> createCollectionWithNElements(5).get(-1));
	}

	@Test
	void testGetWithSizeAsIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> createCollectionWithNElements(5).get(5));
	}

	@Test
	void testGetWithNormalValues() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		for (int index = 0; index < collection.size(); index++) {
			assertEquals(collection.get(index), index);
		}
	}

	@Test
	void testInsertDoesNotOverwrite() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);
		Object[] array = new Object[] { "start", 0, 1, 2, 3, "Test", 4, "stefica" };

		collection.insert("Test", 4);
		collection.insert("start", 0);
		collection.insert("stefica", collection.size());

		assertArrayEquals(array, collection.toArray());
	}

	@Test
	void testInsertWithFullCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(16);
		Object[] array = new Object[] { "start", 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

		collection.insert("start", 0);

		assertArrayEquals(array, collection.toArray());
	}

	@Test
	void testInsertIntoEmptyCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(0);
		Object[] array = new Object[] { "start" };

		collection.insert("start", 0);

		assertArrayEquals(array, collection.toArray());
	}

	@Test
	void testInsertWithNegativeIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> createCollectionWithNElements(5).insert("Test", -1));
	}

	@Test
	void testInsertWithSizePlusOneAsIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> createCollectionWithNElements(5).insert("Test", 6));
	}

	@Test
	void testIndexOfWithEmptyCollection() {
		assertEquals(createCollectionWithNElements(0).indexOf(0), ArrayIndexedCollection.VALUE_NOT_FOUND_RETURN_VALUE);
	}

	@Test
	void testIndexOfNonEmptyCollection() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		for (int index = 0; index < collection.size(); index++) {
			assertEquals(index, collection.indexOf(index));

			assertEquals(ArrayIndexedCollection.VALUE_NOT_FOUND_RETURN_VALUE,
					collection.indexOf(index + 100));
		}
	}

	@Test
	void testIndexOfWithNull() {
		assertEquals(createCollectionWithNElements(5).indexOf(null), -1);
	}

	@Test
	void testIndexOfUsesEquals() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);

		collection.add(new String("Test"));

		assertEquals(collection.size() - 1, collection.indexOf(new String("Test")));
	}

	@Test
	void testRemoveShiftsRemainingValues() {
		ArrayIndexedCollection collection = createCollectionWithNElements(5);
		Object[] array = new Object[] { 0, 2, 4 };

		collection.remove(3);
		collection.remove(1);

		assertArrayEquals(array, collection.toArray());
	}

	@Test
	void testRemoveWithNegativeIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> createCollectionWithNElements(5).remove(-1));
	}

	@Test
	void testRemoveWithSizeAsIndex() {
		assertThrows(IndexOutOfBoundsException.class, () -> createCollectionWithNElements(5).remove(6));
	}

	@Test
	void testRemoveIndexWithEmptyCollection() {
		assertThrows(IndexOutOfBoundsException.class, () -> createCollectionWithNElements(0).remove(0));
	}

}
