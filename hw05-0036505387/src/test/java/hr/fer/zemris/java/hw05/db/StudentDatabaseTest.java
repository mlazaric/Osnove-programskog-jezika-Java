package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class StudentDatabaseTest {

	private static IFilter ALL = e -> true;
	private static IFilter NONE = e -> false;

	private static StudentDatabase getDatabase() {
		try {
			Object[] objs = Files.lines(Paths.get("src/test/resources/database.txt")).toArray();
			String[] lines = Arrays.copyOf(objs, objs.length, String[].class);

			return new StudentDatabase(lines);
		} catch (IOException e) {
			throw new RuntimeException(e); // Wrap the checked exception.
		}
	}

	@Test
	void testForJmbagWithValidJmbag() {
		StudentDatabase database = getDatabase();
		StudentRecord record = database.forJMBAG("0000000001");

		assertEquals("0000000001", record.getJmbag());
		assertEquals("Marin", record.getFirstName());
		assertEquals("Akšamović", record.getLastName());
		assertEquals(2, record.getFinalGrade());

		assertEquals(new StudentRecord("0000000001", "Not", "Important", 1), record);
	}

	@Test
	void testForJmbagWithInvalidJmbag() {
		StudentDatabase database = getDatabase();

		assertNull(database.forJMBAG("invalid"));
		assertNull(database.forJMBAG(null));
	}

	@Test
	void testFilterReturnsAll() {
		StudentDatabase database = getDatabase();
		List<StudentRecord> allStudents = database.filter(ALL);

		assertEquals(63, allStudents.size());
	}

	@Test
	void testFilterReturnsNone() {
		StudentDatabase database = getDatabase();
		List<StudentRecord> emptyList = database.filter(NONE);

		assertEquals(0, emptyList.size());
		assertTrue(emptyList.isEmpty());
	}

}
