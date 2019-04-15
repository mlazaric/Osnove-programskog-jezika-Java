package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ComparisonOperatorsTest {

	private static final String LEAST = "0000000001";
	private static final String MIDDLE = "0000010001";
	private static final String GREATEST = "5432010001";

	private static void assertMatchesEqual(IComparisonOperator op) {
		assertTrue(op.satisfied(LEAST, LEAST));
		assertTrue(op.satisfied(MIDDLE, MIDDLE));
		assertTrue(op.satisfied(GREATEST, GREATEST));
	}

	private static void assertDoesNotMatchEqual(IComparisonOperator op) {
		assertFalse(op.satisfied(LEAST, LEAST));
		assertFalse(op.satisfied(MIDDLE, MIDDLE));
		assertFalse(op.satisfied(GREATEST, GREATEST));
	}

	private static void assertMatchesLesserThan(IComparisonOperator op) {
		assertTrue(op.satisfied(LEAST, MIDDLE));
		assertTrue(op.satisfied(MIDDLE, GREATEST));
		assertTrue(op.satisfied(LEAST, GREATEST));
	}

	private static void assertDoesNotMatchLesserThan(IComparisonOperator op) {
		assertFalse(op.satisfied(LEAST, MIDDLE));
		assertFalse(op.satisfied(MIDDLE, GREATEST));
		assertFalse(op.satisfied(LEAST, GREATEST));
	}

	private static void assertMatchesGreaterThan(IComparisonOperator op) {
		assertTrue(op.satisfied(MIDDLE, LEAST));
		assertTrue(op.satisfied(GREATEST, MIDDLE));
		assertTrue(op.satisfied(GREATEST, LEAST));
	}

	private static void assertDoesNotMatchGreaterThan(IComparisonOperator op) {
		assertFalse(op.satisfied(MIDDLE, LEAST));
		assertFalse(op.satisfied(GREATEST, MIDDLE));
		assertFalse(op.satisfied(GREATEST, LEAST));
	}

	@Test
	void testLikeOperatorWithExamplesFromThePDF() {
		assertFalse(ComparisonOperators.LIKE.satisfied("Zagreb", "Aba*"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"));
	}

	@Test
	void testLikeOperatorWithMultipleStars() {
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AAAA", "AA**AA"));
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AAAA", "A*A*AA"));
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AAAA", "*AAAA*"));
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AAAA", "**AAAA"));
		assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AAAA", "AAAA**"));
	}

	@Test
	void testLikeOperatorWithPrefix() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AA", "AA*"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AAB", "AA*"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AB", "AA*"));
		assertFalse(ComparisonOperators.LIKE.satisfied("BA", "AA*"));
	}

	@Test
	void testLikeOperatorWithSuffix() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AA", "*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("BAA", "*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "*AA"));
		assertFalse(ComparisonOperators.LIKE.satisfied("BA", "*AA"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AB", "*AA"));
	}

	@Test
	void testLikeOperatorWithPrefixAndSuffix() {
		assertFalse(ComparisonOperators.LIKE.satisfied("AA", "AA*AA"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("ABCBA", "AB*BA"));
	}

	@Test
	void testLikeOperatorWithNoStars() {
		assertFalse(ComparisonOperators.LIKE.satisfied("AA", "AAAA"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AAAA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AAAA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("ABCBA", "ABCBA"));
	}

	@Test
	void testLessOperator() {
		assertMatchesLesserThan(ComparisonOperators.LESS);
		assertDoesNotMatchEqual(ComparisonOperators.LESS);
		assertDoesNotMatchGreaterThan(ComparisonOperators.LESS);
	}

	@Test
	void testEqualsOperator() {
		assertDoesNotMatchLesserThan(ComparisonOperators.EQUALS);
		assertMatchesEqual(ComparisonOperators.EQUALS);
		assertDoesNotMatchGreaterThan(ComparisonOperators.EQUALS);
	}

	@Test
	void testGreaterOperator() {
		assertDoesNotMatchLesserThan(ComparisonOperators.GREATER);
		assertDoesNotMatchEqual(ComparisonOperators.GREATER);
		assertMatchesGreaterThan(ComparisonOperators.GREATER);
	}

	@Test
	void testLessOrEqualsOperator() {
		assertMatchesLesserThan(ComparisonOperators.LESS_OR_EQUALS);
		assertMatchesEqual(ComparisonOperators.LESS_OR_EQUALS);
		assertDoesNotMatchGreaterThan(ComparisonOperators.LESS_OR_EQUALS);
	}

	@Test
	void testNotEqualsOperator() {
		assertMatchesLesserThan(ComparisonOperators.NOT_EQUALS);
		assertDoesNotMatchEqual(ComparisonOperators.NOT_EQUALS);
		assertMatchesGreaterThan(ComparisonOperators.NOT_EQUALS);
	}

	@Test
	void testGreaterOrEqualsOperator() {
		assertDoesNotMatchLesserThan(ComparisonOperators.GREATER_OR_EQUALS);
		assertMatchesEqual(ComparisonOperators.GREATER_OR_EQUALS);
		assertMatchesGreaterThan(ComparisonOperators.GREATER_OR_EQUALS);
	}

}
