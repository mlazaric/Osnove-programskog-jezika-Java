package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class FactorialTest {

	@Test
	public void testLowerLimit() {
		long factorial = Factorial.factorial(3);

		assertEquals(1 * 2 * 3, factorial);
	}

	@Test
	public void testUpperLimit() {
		long factorial = Factorial.factorial(20);
		long expectedValue = 1;

		for (int i = 1; i <= 20; ++i) {
			expectedValue *= i;
		}

		assertEquals(expectedValue, factorial);
	}

	@Test
	public void testMiddle() {
		long factorial = Factorial.factorial(10);

		assertEquals(1 * 2 * 3 * 4 * 5 * 6 * 7 * 8 * 9 * 10, factorial);
	}

	@Test
	public void testUnderLowerLimit() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.factorial(-1));
	}

	@Test
	public void testOverUpperLimit() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.factorial(21));
	}

	@Test
	public void testZero() {
		assertEquals(1, Factorial.factorial(0));
	}

	@Test
	public void testNegativeNumber() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.factorial(-5));
	}

}