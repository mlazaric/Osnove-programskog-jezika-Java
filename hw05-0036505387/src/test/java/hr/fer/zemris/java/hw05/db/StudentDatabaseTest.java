package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

class StudentDatabaseTest {

	// Query tests are in QueryFilterTest

	static IFilter ALL = e -> true;
	static IFilter NONE = e -> false;

	static StudentDatabase getDatabase() {
		try {
			List<String> lines = Files.readAllLines(
					Paths.get("src/test/resources/database.txt"),
					StandardCharsets.UTF_8
					);

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
