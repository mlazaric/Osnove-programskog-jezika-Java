package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple database of students.
 *
 * @author Marko Lazarić
 *
 */
public class StudentDatabase {

	/**
	 * JMBAG to student mapping.
	 */
	private final Map<String, StudentRecord> database;

	/**
	 * The students in this database.
	 */
	private final List<StudentRecord> listOfStudents;

	/**
	 * Creates a new {@link StudentDatabase} with the given records.
	 *
	 * @param lines each line is one student record
	 *
	 * @throws NullPointerException if {@code lines} is {@code null}
	 * @throws IllegalArgumentException if a line cannot be parsed, if there are duplicates or if there is an invalid grade
	 */
	public StudentDatabase(List<String> lines) {
		Objects.requireNonNull(lines);

		database = new HashMap<>();
		listOfStudents = new ArrayList<>(lines.size());

		for (String line : lines) {
			StudentRecord record = parseRecord(line);

			database.put(record.getJmbag(), record);
			listOfStudents.add(record);
		}
	}

	/**
	 * Parses a single {@link StudentRecord} from a line.
	 *
	 * @param record the line to parse into a {@link StudentRecord}
	 * @return the parsed {@link StudentRecord}
	 *
	 * @throws IllegalArgumentException if it is a duplicate, if there is an invalid number of arguments or if there is an invalid grade
	 */
	private StudentRecord parseRecord(String record) {
		String[] parts = record.split("\\t");

		if (database.containsKey(parts[0])) {
			throw new IllegalArgumentException("'" + parts[0] + "' is a duplicate JMBAG.");
		}

		if (parts.length != 4) {
			throw new IllegalArgumentException("Wrong number of arguments in line '" + record + "'.");
		}

		try {
			return new StudentRecord(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("'" + parts[3] + "' is not a valid grade.");
		}
	}

	/**
	 * Returns the {@link StudentRecord} for that JMBAG.
	 *
	 * @param jmbag the student's JMBAG
	 * @return student whose JMBAG it is or null
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return database.get(jmbag);
	}

	/**
	 * Applies a filter to all the {@link StudentRecord}s in the database and returns the result.
	 *
	 * @param filter the filter to apply
	 * @return the filtered records
	 *
	 * @throws NullPointerException if {@code filter} is {@code null}
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull(filter);

		List<StudentRecord> filtered = new LinkedList<>();

		for (StudentRecord studentRecord : listOfStudents) {
			if (filter.accepts(studentRecord)) {
				filtered.add(studentRecord);
			}
		}

		return filtered;
	}

}
