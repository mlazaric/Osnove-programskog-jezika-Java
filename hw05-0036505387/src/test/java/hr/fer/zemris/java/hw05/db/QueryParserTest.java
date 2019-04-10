package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class QueryParserTest {

	@Test
	void testExampleFromPDF1() {
		QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");

		assertTrue(qp1.isDirectQuery());
		assertEquals("0123456789", qp1.getQueriedJMBAG());
		assertEquals(1, qp1.getQuery().size());
	}

	@Test
	void testExampleFromPDF2() {
		QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");

		assertFalse(qp2.isDirectQuery());
		assertThrows(IllegalStateException.class, qp2::getQueriedJMBAG);
		assertEquals(2, qp2.getQuery().size());
	}

	@Test
	void testParsingIgnoresWhitespace() {
		List<ConditionalExpression> query = new QueryParser(" lastName=\"Bosnić\"").getQuery();

		String[] sameQueries = {
				" lastName=\"Bosnić\"",
				" lastName =\"Bosnić\"",
				" lastName= \"Bosnić\"",
				" lastName = \"Bosnić\""
		};

		for (String input : sameQueries) {
			assertEquals(query, new QueryParser(input).getQuery());
		}
	}

	@Test
	void testDirectQueryFromPDF() {
		List<ConditionalExpression> query = new QueryParser("jmbag = \"0000000003\"").getQuery();

		assertEquals(1, query.size());

		ConditionalExpression actual = query.get(0);
		ConditionalExpression expected = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000003", ComparisonOperators.EQUALS);

		assertEquals(expected, actual);
	}

	@Test
	void testTwoPartQueryFromPDF1() {
		List<ConditionalExpression> query = new QueryParser("jmbag = \"0000000003\" AND lastName LIKE \"B*\"").getQuery();

		assertEquals(2, query.size());

		ConditionalExpression expected1 = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000003", ComparisonOperators.EQUALS);
		ConditionalExpression expected2 = new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*", ComparisonOperators.LIKE);

		assertEquals(expected1, query.get(0));
		assertEquals(expected2, query.get(1));
	}

	@Test
	void testTwoPartQueryFromPDF2() {
		List<ConditionalExpression> query = new QueryParser(" jmbag = \"0000000003\" AND lastName LIKE \"L*\"").getQuery();

		assertEquals(2, query.size());

		ConditionalExpression expected1 = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000003", ComparisonOperators.EQUALS);
		ConditionalExpression expected2 = new ConditionalExpression(FieldValueGetters.LAST_NAME, "L*", ComparisonOperators.LIKE);

		assertEquals(expected1, query.get(0));
		assertEquals(expected2, query.get(1));
	}

	@Test
	void testOnePartQueryFromPDF1() {
		List<ConditionalExpression> query = new QueryParser("lastName LIKE \"B*\"").getQuery();

		assertEquals(1, query.size());

		ConditionalExpression expected = new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*", ComparisonOperators.LIKE);

		assertEquals(expected, query.get(0));
	}

	@Test
	void testOnePartQueryFromPDF2() {
		List<ConditionalExpression> query = new QueryParser("lastName LIKE \"Be*\"").getQuery();

		assertEquals(1, query.size());

		ConditionalExpression expected = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Be*", ComparisonOperators.LIKE);

		assertEquals(expected, query.get(0));
	}

	@Test
	void testInvalidOperator() {
		assertThrows(IllegalArgumentException.class, () -> new QueryParser("lastName ^ \"something\""));
	}

	@Test
	void testInvalidFieldName() {
		assertThrows(IllegalArgumentException.class, () -> new QueryParser("middleName = \"something\""));
	}

	@Test
	void testFieldNameIsCaseSensitive() {
		assertThrows(IllegalArgumentException.class, () -> new QueryParser("lastname = \"something\""));
	}

	@Test
	void testThirdArgumentHasToBeAStringLiteral() {
		assertThrows(IllegalArgumentException.class, () -> new QueryParser("lastName = something"));
	}

	@Test
	void testAndIsCaseInsensitive() { // If any of these throw an exception, the test will fail
		new QueryParser("lastName = \"something\" AND lastName = \"something\"");
		new QueryParser("lastName = \"something\" aNd lastName = \"something\"");
		new QueryParser("lastName = \"something\" AnD lastName = \"something\"");
		new QueryParser("lastName = \"something\" and lastName = \"something\"");
	}

	@Test
	void testAllComparisonOperatorsAreParsed() {
		assertDoesNotThrow(() -> new QueryParser("lastName < \"something\""), "Less operator is not being parsed correctly.");
		assertDoesNotThrow(() -> new QueryParser("lastName > \"something\""), "Greater than operator is not being parsed correctly.");
		assertDoesNotThrow(() -> new QueryParser("lastName = \"something\""), "Equals operator is not being parsed correctly.");

		assertDoesNotThrow(() -> new QueryParser("lastName <= \"something\""), "Less than or equal to operator is not being parsed correctly.");
		assertDoesNotThrow(() -> new QueryParser("lastName >= \"something\""), "Greater than or equal to operator is not being parsed correctly.");
		assertDoesNotThrow(() -> new QueryParser("lastName != \"something\""), "Not equal operator is not being parsed correctly.");

		assertDoesNotThrow(() -> new QueryParser("lastName LIKE \"something\""), "LIKE is not being parsed correctly.");
	}

}
