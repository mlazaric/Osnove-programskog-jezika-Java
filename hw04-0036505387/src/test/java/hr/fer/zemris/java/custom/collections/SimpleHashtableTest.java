package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.collections.SimpleHashtable.TableEntry;

class SimpleHashtableTest {

	SimpleHashtable<String, String> createHashTableWithNElements(int number) {
		SimpleHashtable<String, String> table = new SimpleHashtable<>();

		for (int index = 0; index < number; index++) {
			table.put("" + index, "" + index);
		}

		return table;
	}

	@Test
	void testExampleFromPDF() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		// query collection:
		Integer kristinaGrade = examMarks.get("Kristina");
		assertEquals(5, kristinaGrade); // writes: 5

		// What is collection's size? Must be four!
		assertEquals(4, examMarks.size()); // writes: 4
	}

	@Test
	void testConstructorWithNoArguments() {
		assertEquals(SimpleHashtable.DEFAULT_CAPACITY, new SimpleHashtable<Integer, String>().getCapacity());
	}

	@Test
	void testCapacityConstructorWithZero() {
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<Integer, Integer>(0));
	}

	@Test
	void testCapacityConstructorWithNegativeNumber() {
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<Integer, Integer>(-1));
	}

	@Test
	void testCapacityConstructorWithNonPowersOf2() {
		assertEquals(4, new SimpleHashtable<Object, Object>(3).getCapacity());
		assertEquals(32, new SimpleHashtable<Object, Object>(25).getCapacity());
		assertEquals(128, new SimpleHashtable<Object, Object>(127).getCapacity());
		assertEquals(256, new SimpleHashtable<Object, Object>(129).getCapacity());
	}

	@Test
	void testCapacityConstructorWithPowersOf2() {
		assertEquals(1, new SimpleHashtable<Object, Object>(1).getCapacity());
		assertEquals(2, new SimpleHashtable<Object, Object>(2).getCapacity());
		assertEquals(4, new SimpleHashtable<Object, Object>(4).getCapacity());
		assertEquals(32, new SimpleHashtable<Object, Object>(32).getCapacity());
		assertEquals(256, new SimpleHashtable<Object, Object>(256).getCapacity());
	}

	@Test
	void testPutWithNullKey() {
		assertThrows(NullPointerException.class, () -> createHashTableWithNElements(5).put(null, "stefica"));
	}

	@Test
	void testPutWithNullValue() {
		var table = createHashTableWithNElements(5);

		assertEquals(5, table.size());
		table.put("Test", null);
		assertEquals(6, table.size());

		assertNull(table.get("Test"));
	}

	@Test
	void testPutAddsNewEntry() {
		var table = createHashTableWithNElements(5);

		assertEquals(5, table.size());
		table.put("Test", "stefica");
		assertEquals(6, table.size());
		table.put("Test2", "stefica2");
		assertEquals(7, table.size());
		table.put("Test3", "stefica3");
		assertEquals(8, table.size());

		assertEquals("stefica", table.get("Test"));
		assertEquals("stefica2", table.get("Test2"));
		assertEquals("stefica3", table.get("Test3"));
	}

	@Test
	void testPutUpdatesExistingEntry() {
		var table = createHashTableWithNElements(5);

		assertEquals(5, table.size());
		table.put("Test", "stefica");
		assertEquals(6, table.size());
		table.put("Test2", "stefica2");
		assertEquals(7, table.size());
		table.put("Test3", "stefica3");
		assertEquals(8, table.size());

		table.put("Test", "java");
		assertEquals(8, table.size());
		table.put("Test2", "scala");
		assertEquals(8, table.size());
		table.put("Test3", "kotlin");
		assertEquals(8, table.size());

		assertEquals("java", table.get("Test"));
		assertEquals("scala", table.get("Test2"));
		assertEquals("kotlin", table.get("Test3"));
	}

	@Test
	void testSizeOfEmptyTable() {
		assertEquals(0, createHashTableWithNElements(0).size());
	}

	@Test
	void testSizeOfNonEmptyTable() {
		assertEquals(10, createHashTableWithNElements(10).size());
	}

	@Test
	void testContainsKeyWithExistingKeys() {
		var table = createHashTableWithNElements(5);

		for (int index = 0; index < 5; index++) {
			assertTrue(table.containsKey("" + index));
		}
	}

	@Test
	void testContainsKeyWithNonExistingKeys() {
		var table = createHashTableWithNElements(5);

		for (int index = 0; index < 5; index++) {
			assertFalse(table.containsKey("" + (index + 10)));
		}
	}

	@Test
	void testContainsKeyWithNull() {
		var table = createHashTableWithNElements(5);

		assertFalse(table.containsKey(null));
	}

	@Test
	void testContainsValueWithExistingValues() {
		var table = createHashTableWithNElements(5);

		for (int index = 0; index < 5; index++) {
			assertTrue(table.containsValue("" + index));
		}
	}

	@Test
	void testContainsValueWithNonExistingKeys() {
		var table = createHashTableWithNElements(5);

		for (int index = 0; index < 5; index++) {
			assertFalse(table.containsValue("" + (index + 10)));
		}
	}

	@Test
	void testContainsValueWithNull() {
		var table = createHashTableWithNElements(5);

		assertFalse(table.containsKey(null));

		table.put("stefica", null);

		assertTrue(table.containsValue(null));
	}

	@Test
	void testRemoveOfExistingKeys() {
		var table = createHashTableWithNElements(5);

		assertEquals(5, table.size());

		table.remove("1");
		table.remove("4");

		assertEquals(3, table.size());

		assertEquals("0", table.get("0"));
		assertNull(table.get("1"));
		assertEquals("2", table.get("2"));
		assertEquals("3", table.get("3"));
		assertNull(table.get("4"));
	}

	@Test
	void testRemoveOfNonExistingKeys() {
		var table = createHashTableWithNElements(5);

		assertEquals(5, table.size());

		table.remove("100");
		table.remove("stefica");
		table.remove("java");

		assertEquals(5, table.size());

		assertEquals("0", table.get("0"));
		assertEquals("1", table.get("1"));
		assertEquals("2", table.get("2"));
		assertEquals("3", table.get("3"));
		assertEquals("4", table.get("4"));
	}

	@Test
	void testRemoveOfNull() {
		var table = createHashTableWithNElements(5);

		assertEquals(5, table.size());

		table.remove(null);

		assertEquals(5, table.size());
	}

	@Test
	void testIsEmptyWithEmptyTable() {
		assertTrue(createHashTableWithNElements(0).isEmpty());
	}

	@Test
	void testIsEmptyWIthNonEmptyTable() {
		assertFalse(createHashTableWithNElements(5).isEmpty());
	}

	@Test
	void testDoublesSize() {
		var table = new SimpleHashtable<Integer, Integer>(1);

		assertEquals(1, table.getCapacity());

		table.put(1, 1); // 1 / 1 slots filled = 100% - needs to double the capacity

		assertEquals(2, table.getCapacity());

		table.put(2, 2); // 2 / 2 slots filled = 100% - needs to double the capacity

		assertEquals(4, table.getCapacity());

		table.put(3, 3); // 3 / 4 slots filled = 75% - needs to double the capacity

		assertEquals(8, table.getCapacity());

		table.put(4, 4); // 4 / 8 slots filled = 50%

		assertEquals(8, table.getCapacity());

		table.put(5, 5); // 5 / 8 slots filled = 62.5%

		assertEquals(8, table.getCapacity());

		table.put(6, 6); // 6 / 8 slots filled = 75% - needs to double the capacity

		assertEquals(16, table.getCapacity());
	}

	@SuppressWarnings("unchecked")
	@Test
	void testIteratingExampleFromPDF() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		SimpleHashtable.TableEntry<String, Integer>[] table = new TableEntry[]{
				new SimpleHashtable.TableEntry<>("Ante", 2),
				new SimpleHashtable.TableEntry<>("Ivana", 5),
				new SimpleHashtable.TableEntry<>("Jasna", 2),
				new SimpleHashtable.TableEntry<>("Kristina", 5),
		};

		int index = 0;

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		for(SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
			assertEquals(table[index], pair);

			++index;
		}
	}

	@Test
	void testIteratingAndRemovingElementThroughIteratorOnce() {
		var table = createHashTableWithNElements(10);
		var iterator = table.iterator();

		while (iterator.hasNext()) {
			var current = iterator.next();

			if (current.getKey().equals("5")) {
				iterator.remove();
			}
		}

		assertEquals(9, table.size());
	}

	@Test
	void testIteratingAndRemovingElementThroughIteratorTwice() {
		var table = createHashTableWithNElements(10);
		var iterator = table.iterator();

		while (iterator.hasNext()) {
			var current = iterator.next();

			if (current.getKey().equals("5")) {
				iterator.remove();

				assertThrows(IllegalStateException.class, iterator::remove);
			}
		}
	}

	@Test
	void testIteratingAndRemovingElementThroughTheHashTable() {
		var table = createHashTableWithNElements(10);
		var iterator = table.iterator();

		while (iterator.hasNext()) {
			var current = iterator.next();

			if (current.getKey().equals("5")) {
				table.remove("0");

				assertThrows(ConcurrentModificationException.class, iterator::hasNext);
				assertThrows(ConcurrentModificationException.class, iterator::next);
				assertThrows(ConcurrentModificationException.class, iterator::remove);

				return;
			}
		}
	}

	@Test
	void testIteratingAndRemovingExampleFromPDF() {
		var table = createHashTableWithNElements(5);
		var iter = table.iterator();

		while(iter.hasNext()) {
			iter.next();

			iter.remove();
		}

		assertEquals(0, table.size());
		assertTrue(table.isEmpty());
	}
}
