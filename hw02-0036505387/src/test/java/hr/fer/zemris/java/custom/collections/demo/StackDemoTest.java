package hr.fer.zemris.java.custom.collections.demo;

import static hr.fer.zemris.java.custom.collections.demo.StackDemo.evaluatePostfixExpression;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StackDemoTest {

	@Test
	void testEvaluatingValidExpressions() {
		assertEquals(4, evaluatePostfixExpression("8 2 /"));
		assertEquals(3, evaluatePostfixExpression("-1   8 2 / +"));
		assertEquals(4, evaluatePostfixExpression("8 -2 / -1 *"));
	}

	@Test
	void testEvaluatingAnExpressionWithTooManyNumbers() {
		assertThrows(IllegalArgumentException.class,
				() -> evaluatePostfixExpression("1 1 1 +"));
	}

	@Test
	void testEvaluatingAnExpressionWithTooFewNumbers() {
		assertThrows(IllegalArgumentException.class,
				() -> evaluatePostfixExpression("1 +"));
	}

	@Test
	void testEvaluatingAnExpressionWhichDividesByZero() {
		assertThrows(IllegalArgumentException.class,
				() -> evaluatePostfixExpression("1 0 /"));
		assertThrows(IllegalArgumentException.class,
				() -> evaluatePostfixExpression("1 0 %"));
	}

	@Test
	void testEvaluatingAnExpressionWithAnUnsupportedOperator() {
		assertThrows(IllegalArgumentException.class,
				() -> evaluatePostfixExpression("1 1 ^"));
		assertThrows(IllegalArgumentException.class,
				() -> evaluatePostfixExpression("1 1 ++"));
	}


}
