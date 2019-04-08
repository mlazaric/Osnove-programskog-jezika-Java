package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConditionalExpressionTest {

	@Test
	void testFieldValueGetterCannotBeNull() {
		assertThrows(NullPointerException.class, () -> new ConditionalExpression(null, " ", ComparisonOperators.LESS));
	}

	@Test
	void testStringLiteralCannotBeNull() {
		assertThrows(NullPointerException.class, () -> new ConditionalExpression(FieldValueGetters.FIRST_NAME, null, ComparisonOperators.LESS));
	}

	@Test
	void testComparisonOperatorCannotBeNull() {
		assertThrows(NullPointerException.class, () -> new ConditionalExpression(FieldValueGetters.FIRST_NAME, " ", null));
	}

	@Test
	void testTrueConditionalExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME,
				"Bos*",
				ComparisonOperators.LIKE
				);

		StudentRecord record = new StudentRecord("0", "Boston", "Isaac", 1);

		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record), // returns lastName from given record
				expr.getStringLiteral() // returns "Bos*"
				);

		assertEquals(FieldValueGetters.LAST_NAME, expr.getFieldGetter());
		assertEquals("Bos*", expr.getStringLiteral());
		assertEquals(ComparisonOperators.LIKE, expr.getComparisonOperator());

		assertTrue(recordSatisfies);
	}

	@Test
	void testFalseConditionalExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME,
				"Bos*",
				ComparisonOperators.LIKE
				);

		StudentRecord record = new StudentRecord("0", "Mirkovic", "Isaac", 1);

		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record), // returns lastName from given record
				expr.getStringLiteral() // returns "Bos*"
				);

		assertEquals(FieldValueGetters.LAST_NAME, expr.getFieldGetter());
		assertEquals("Bos*", expr.getStringLiteral());
		assertEquals(ComparisonOperators.LIKE, expr.getComparisonOperator());

		assertFalse(recordSatisfies);
	}

}
