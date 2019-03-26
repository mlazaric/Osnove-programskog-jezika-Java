package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.collections.demo.EvenIntegerTester;

class CollectionTest {
	Collection col, col1, col2;

	/**
	 * Do not run this file, run {@link ArrayIndexedCollectionTest}
	 * and {@link LinkedListIndexedCollectionTest}.
	 *
	 * This is a helper file so there is less code duplication.
	 */
	@BeforeEach
	void beforeEach() {
		// By default use ArrayIndexedCollection so it doesn't fail.
		// But this method is overriden.
		col = new ArrayIndexedCollection();
		col1 = new ArrayIndexedCollection();
		col2 = new ArrayIndexedCollection();
	}

	@Test
	void demo1() {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();

		assertTrue(getter.hasNextElement());
		assertEquals("Ivo", getter.getNextElement());

		assertTrue(getter.hasNextElement());
		assertEquals("Ana", getter.getNextElement());

		assertTrue(getter.hasNextElement());
		assertEquals("Jasna", getter.getNextElement());

		assertFalse(getter.hasNextElement());
		assertThrows(NoSuchElementException.class, getter::getNextElement);
	}

	@Test
	void demo2() {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();

		assertTrue(getter.hasNextElement());
		assertTrue(getter.hasNextElement());
		assertTrue(getter.hasNextElement());
		assertTrue(getter.hasNextElement());
		assertTrue(getter.hasNextElement());

		assertEquals("Ivo", getter.getNextElement());

		assertTrue(getter.hasNextElement());
		assertTrue(getter.hasNextElement());

		assertEquals("Ana", getter.getNextElement());

		assertTrue(getter.hasNextElement());
		assertTrue(getter.hasNextElement());
		assertTrue(getter.hasNextElement());

		assertEquals("Jasna", getter.getNextElement());

		assertFalse(getter.hasNextElement());
		assertFalse(getter.hasNextElement());
	}

	@Test
	void demo3() {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();


		assertEquals("Ivo", getter.getNextElement());
		assertEquals("Ana", getter.getNextElement());
		assertEquals("Jasna", getter.getNextElement());

		assertThrows(NoSuchElementException.class, getter::getNextElement);
	}

	@Test
	void demo4() {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter1 = col.createElementsGetter();
		ElementsGetter getter2 = col.createElementsGetter();

		assertEquals("Ivo", getter1.getNextElement());
		assertEquals("Ana", getter1.getNextElement());
		assertEquals("Ivo", getter2.getNextElement());
		assertEquals("Jasna", getter1.getNextElement());
		assertEquals("Ana", getter2.getNextElement());
	}

	@Test
	void demo5() {
		col1.add("Ivo");
		col1.add("Ana");
		col1.add("Jasna");

		col2.add("Jasmina");
		col2.add("Štefanija");
		col2.add("Karmela");

		ElementsGetter getter1 = col1.createElementsGetter();
		ElementsGetter getter2 = col1.createElementsGetter();
		ElementsGetter getter3 = col2.createElementsGetter();

		assertEquals("Ivo", getter1.getNextElement());
		assertEquals("Ana", getter1.getNextElement());
		assertEquals("Ivo", getter2.getNextElement());
		assertEquals("Jasmina", getter3.getNextElement());
		assertEquals("Štefanija", getter3.getNextElement());
	}

	@Test
	void demo6() {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();

		assertEquals("Ivo", getter.getNextElement());
		assertEquals("Ana", getter.getNextElement());

		col.clear();

		assertThrows(ConcurrentModificationException.class, getter::getNextElement);
	}

	@Test
	void demo7() {
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();

		getter.getNextElement();

		getter.processRemaining(new Processor() {

			int index = 0;

			@Override
			public void process(Object value) {
				if (index == 0) {
					assertEquals("Ana", value);
				}
				else {
					assertEquals("Jasna", value);
				}

				++index;
			}
		});
	}

	@Test
	void demo8() {
		col1.add(2);
		col1.add(3);
		col1.add(4);
		col1.add(5);
		col1.add(6);

		col2.add(12);
		col2.addAllSatisfying(col1, new EvenIntegerTester());

		col2.forEach(new Processor() {

			int index = 0;
			int[] values = new int[] { 12, 2, 4, 6 };

			@Override
			public void process(Object value) {
				assertEquals(values[index], value);

				++index;
			}
		});
	}

	@Test
	void demo9() {
		List col1 = new ArrayIndexedCollection();
		List col2 = new LinkedListIndexedCollection();

		col1.add("Ivana");
		col2.add("Jasna");

		Collection col3 = col1;
		Collection col4 = col2;

		col1.get(0);
		col2.get(0);

		//col3.get(0); // neće se prevesti! Razumijete li zašto? Collection nema get metodu.
		//col4.get(0); // neće se prevesti! Razumijete li zašto?

		col1.forEach((obj) -> assertEquals("Ivana", obj)); // Ivana
		col2.forEach((obj) -> assertEquals("Jasna", obj)); // Jasna
		col3.forEach((obj) -> assertEquals("Ivana", obj)); // Ivana
		col4.forEach((obj) -> assertEquals("Jasna", obj)); // Jasna
	}
}
