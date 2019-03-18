package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LinkedListIndexedCollectionTest {

	static LinkedListIndexedCollection createCollectionWithNElements(int numberOfElements) {
		LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

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
		assertEquals(createCollectionWithNElements(0).size(), 0);
	}

	@Test
	void testSizeWithNonEmptyCollection() {
		assertEquals(createCollectionWithNElements(5).size(), 5);
	}

	@Test
	void testAddWithNotFullCollection() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

		assertEquals(collection.size(), 5);

		collection.add("Test");

		assertEquals(collection.size(), 6);
		assertTrue(collection.contains("Test"));
	}

	@Test
	void testAddWithFullCollection() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(16);

		assertEquals(collection.size(), 16);

		collection.add("Test");

		assertEquals(collection.size(), 17);
		assertTrue(collection.contains("Test"));
	}

	@Test
	void testAddWithDuplicate() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

		assertEquals(collection.size(), 5);

		collection.add("Test");
		collection.add("Test");

		assertEquals(collection.size(), 7);
	}

	@Test
	void testAddWithNull() {
		assertThrows(NullPointerException.class, () -> createCollectionWithNElements(0).add(null));
	}

	@Test
	void testAddInsertsAtTheEnd() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

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
		LinkedListIndexedCollection collection = createCollectionWithNElements(1);

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
		LinkedListIndexedCollection collection = createCollectionWithNElements(1);

		collection.add(new String("Test"));

		assertEquals(collection.size(), 2);
		assertTrue(collection.remove(new String("Test")));
		assertEquals(collection.size(), 1);
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
		LinkedListIndexedCollection collection = createCollectionWithNElements(1);

		collection.add(new String("Test"));
		collection.add(new String("Test"));

		assertEquals(collection.size(), 3);
		assertTrue(collection.remove(new String("Test")));
		assertEquals(collection.size(), 2);
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
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

		collection.addAll(createCollectionWithNElements(0));

		assertEquals(collection.size(), 5);
	}

	@Test
	void testAddAllWithNull() {
		assertThrows(NullPointerException.class, () -> createCollectionWithNElements(5).addAll(null));
	}

	@Test
	void testAddAllWithNonEmptyCollection() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

		collection.addAll(createCollectionWithNElements(5));

		assertEquals(collection.size(), 10);
	}

	@Test
	void testClearWithEmptyCollection() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(0);

		collection.clear();

		assertEquals(collection.size(), 0);
		assertArrayEquals(new Object[] {}, collection.toArray());
	}

	@Test
	void testClearWithNonEmptyCollection() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

		collection.clear();

		assertEquals(collection.size(), 0);
		assertArrayEquals(new Object[] {}, collection.toArray());
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
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

		for (int index = 0; index < collection.size(); index++) {
			assertEquals(collection.get(index), index);
		}
	}

	@Test
	void testInsertDoesNotOverwrite() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);
		Object[] array = new Object[] { "start", 0, 1, 2, 3, "Test", 4, "stefica" };

		collection.insert("Test", 4);
		collection.insert("start", 0);
		collection.insert("stefica", collection.size());

		assertArrayEquals(array, collection.toArray());
	}

	@Test
	void testInsertWithFullCollection() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(16);
		Object[] array = new Object[] { "start", 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

		collection.insert("start", 0);

		assertArrayEquals(array, collection.toArray());
	}

	@Test
	void testInsertIntoEmptyCollection() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(0);
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
		assertEquals(createCollectionWithNElements(0).indexOf(0), -1);
	}

	@Test
	void testIndexOfNonEmptyCollection() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

		for (int index = 0; index < collection.size(); index++) {
			assertEquals(collection.indexOf(index), index);

			assertEquals(collection.indexOf(index + 100), -1);
		}
	}

	@Test
	void testIndexOfWithNull() {
		assertEquals(createCollectionWithNElements(5).indexOf(null), -1);
	}

	@Test
	void testIndexOfUsesEquals() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);

		collection.add(new String("Test"));

		assertEquals(collection.indexOf(new String("Test")), collection.size() - 1);
	}

	@Test
	void testRemoveShiftsRemainingValues() {
		LinkedListIndexedCollection collection = createCollectionWithNElements(5);
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
