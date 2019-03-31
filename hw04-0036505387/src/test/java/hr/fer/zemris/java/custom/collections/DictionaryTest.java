package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DictionaryTest {

	static Dictionary<Integer, Integer> createDictionaryWithNEntries(int numberOfElements) {
		Dictionary<Integer, Integer> dictionary = new Dictionary<>();

		for (int index = 0; index < numberOfElements; index++) {
			dictionary.put(index, index);
		}

		return dictionary;
	}

	// isEmpty tests

	@Test
	void testIsEmptyWithEmptyDictionary() {
		assertTrue(createDictionaryWithNEntries(0).isEmpty());
	}

	@Test
	void testIsEmptyWithNonEmptyDictionary() {
		assertFalse(createDictionaryWithNEntries(1).isEmpty());
	}

	// Size tests

	@Test
	void testSizeOfEmptyDictionary() {
		assertEquals(0, createDictionaryWithNEntries(0).size());
	}

	@Test
	void testSizeOfNonEmptyDictionary() {
		assertEquals(5, createDictionaryWithNEntries(5).size());
	}

	// Clear tests

	@Test
	void testClearOfEmptyDictionary() {
		var dictionary = createDictionaryWithNEntries(0);

		assertEquals(0, dictionary.size());

		dictionary.clear();

		assertEquals(0, dictionary.size());
	}

	@Test
	void testClearOfNonEmptyDictionary() {
		var dictionary = createDictionaryWithNEntries(5);

		assertEquals(5, dictionary.size());

		dictionary.clear();

		assertEquals(0, dictionary.size());
	}

	// Put tests

	@Test
	void testPutWithNullKey() {
		assertThrows(NullPointerException.class, () -> createDictionaryWithNEntries(0).put(null, 3));
	}

	@Test
	void testPutWithNullValue() {
		var dictionary = createDictionaryWithNEntries(5);

		assertEquals(5, dictionary.size());

		dictionary.put(500, null);

		assertEquals(6, dictionary.size());
		assertNull(dictionary.get(5));
	}

	@Test
	void testPutWithDuplicatesUpdatesExisting() {
		var dictionary = createDictionaryWithNEntries(5);

		assertEquals(5, dictionary.size());

		dictionary.put(2, 100);
		dictionary.put(3, 500);

		assertEquals(5, dictionary.size());
		assertEquals(100, dictionary.get(2));
		assertEquals(500, dictionary.get(3));
	}

	@Test
	void testPutIncreasesSize() {
		var dictionary = createDictionaryWithNEntries(5);

		assertEquals(5, dictionary.size());

		dictionary.put(20, 100);
		dictionary.put(30, 500);

		assertEquals(7, dictionary.size());
	}

	// Get tests

	@Test
	void testGetWithNull() {
		assertNull(createDictionaryWithNEntries(5).get(null));
	}

	@Test
	void testGetWithValidKey() {
		var dictionary = createDictionaryWithNEntries(5);

		for (int index = 0; index < 5; index++) {
			assertEquals(index, dictionary.get(index));
		}
	}

	@Test
	void testGetWithInvalidKeysAndEmptyCollection() {
		var keys = new Object[] {
				"štefica",
				'a',
				256,
				3.14,
				new Object()
		};
		var dictionary = createDictionaryWithNEntries(0);

		for (Object key : keys) {
			assertNull(dictionary.get(key));
		}
	}

	@Test
	void testGetWithInvalidKeysAndNonEmptyCollection() {
		var keys = new Object[] {
				"štefica",
				'a',
				256,
				3.14,
				new Object()
		};
		var dictionary = createDictionaryWithNEntries(10);

		for (Object key : keys) {
			assertNull(dictionary.get(key));
		}
	}

}
