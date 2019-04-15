package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class QueryFilterTest {

	static List<StudentRecord> getRecordsFromQuery(String query) {
		QueryFilter filter = new QueryFilter(new QueryParser(query).getQuery());

		return StudentDatabaseTest.getDatabase().filter(filter);
	}

	@Test
	void testQueryFilterWithDirectQuery() {
		QueryFilter filter = new QueryFilter(Arrays.asList(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000003", ComparisonOperators.EQUALS)));

		List<StudentRecord> records = StudentDatabaseTest.getDatabase().filter(filter);
		List<StudentRecord> expected = Arrays.asList(new StudentRecord("0000000003", "Bosnić", "Andrea", 4));

		assertEquals(expected, records);
	}

	@Test
	void testQueryFilterFromPDF1() {
		List<StudentRecord> actual = getRecordsFromQuery("jmbag = \"0000000003\"");
		List<StudentRecord> expected = Arrays.asList(new StudentRecord("0000000003", "Bosnić", "Andrea", 4));

		assertEquals(expected, actual);
	}

	@Test
	void testQueryFilterFromPDF2() {
		List<StudentRecord> actual = getRecordsFromQuery(" jmbag = \"0000000003\" AND lastName LIKE \"B*\"");
		List<StudentRecord> expected = Arrays.asList(new StudentRecord("0000000003", "Bosnić", "Andrea", 4));

		assertEquals(expected, actual);
	}

	@Test
	void testQueryFilterFromPDF3() {
		List<StudentRecord> actual = getRecordsFromQuery(" jmbag = \"0000000003\" AND lastName LIKE \"L*\"");
		List<StudentRecord> expected = Collections.emptyList();

		assertEquals(expected, actual);
	}

	@Test
	void testQueryFilterFromPDF4() {
		List<StudentRecord> actual = getRecordsFromQuery(" lastName LIKE \"B*\"");
		List<StudentRecord> expected = Arrays.asList(
				new StudentRecord("0000000002", "Bakamović", "Petra", 3),
				new StudentRecord("0000000003", "Bosnić", "Andrea", 4),
				new StudentRecord("0000000004", "Božić", "Marin", 4),
				new StudentRecord("0000000005", "Brezović", "Jusufadis", 4));

		assertEquals(expected, actual);
	}

	@Test
	void testQueryFilterFromPDF5() {
		List<StudentRecord> actual = getRecordsFromQuery("lastName LIKE \"Be*\"");
		List<StudentRecord> expected = Collections.emptyList();

		assertEquals(expected, actual);
	}

}
