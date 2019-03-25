package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ObjectStackTest {

	static ObjectStack createStackWithNElements(int numberOfElements) {
		ObjectStack stack = new ObjectStack();

		for (int i = 0; i < numberOfElements; ++i) {
			stack.push(i);
		}

		return stack;
	}

	@Test
	void testIsEmptyWithEmptyStack() {
		assertTrue(createStackWithNElements(0).isEmpty());
	}

	@Test
	void testIsEmptyWithNonEmptyStack() {
		assertFalse(createStackWithNElements(5).isEmpty());
	}

	@Test
	void testSizeWithEmptyStack() {
		assertEquals(0, createStackWithNElements(0).size());
	}

	@Test
	void testSizeWithNonEmptyStack() {
		assertEquals(5, createStackWithNElements(5).size());
	}

	@Test
	void testPushOfNull() {
		assertThrows(NullPointerException.class,
				() -> createStackWithNElements(5).push(null));
	}

	@Test
	void testPushWithNormalValue() {
		ObjectStack stack = createStackWithNElements(0);

		assertEquals(0, stack.size());
		stack.push("A");
		assertEquals(1, stack.size());
		stack.push("B");
		assertEquals(2, stack.size());

		assertEquals("B", stack.pop());
		assertEquals(1, stack.size());
		assertEquals("A", stack.pop());
		assertEquals(0, stack.size());
	}

	@Test
	void testPopWithEmptyStack() {
		assertThrows(EmptyStackException.class,
				() -> createStackWithNElements(0).pop());
	}

	@Test
	void testPopWithNonEmptyStack() {
		ObjectStack stack = createStackWithNElements(5);

		for (int value = 4; value >= 0; --value) {
			assertEquals(value, stack.pop());
		}

		assertThrows(EmptyStackException.class,
				() -> stack.pop());
	}

	@Test
	void testPeekWithEmptyStack() {
		assertThrows(EmptyStackException.class,
				() -> createStackWithNElements(0).peek());
	}

	@Test
	void testPeekWithNonEmptyStack() {
		ObjectStack stack = createStackWithNElements(5);

		for (int value = 10; value >= 0; --value) {
			assertEquals(4, stack.peek());
		}
	}

	@Test
	void testClearWithEmptyStack() {
		ObjectStack stack = createStackWithNElements(0);

		stack.clear();

		assertTrue(stack.isEmpty());
		assertEquals(0, stack.size());
		assertThrows(EmptyStackException.class,
				() -> stack.pop());
		assertThrows(EmptyStackException.class,
				() -> stack.peek());
	}

	@Test
	void testClearWithNonEmptyStack() {
		ObjectStack stack = createStackWithNElements(5);

		assertEquals(5, stack.size());

		stack.clear();

		assertTrue(stack.isEmpty());
		assertEquals(0, stack.size());
		assertThrows(EmptyStackException.class,
				() -> stack.pop());
		assertThrows(EmptyStackException.class,
				() -> stack.peek());
	}

}
