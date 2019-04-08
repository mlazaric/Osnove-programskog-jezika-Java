package hr.fer.zemris.java.hw05.db;

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

}
