package hr.fer.zemris.java.hw05.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StudentDatabase {

	private final Map<String, StudentRecord> database;
	private final List<StudentRecord> listOfStudents;

	public StudentDatabase(String[] lines) {
		database = new HashMap<>();
		listOfStudents = new LinkedList<>();

		for (String line : lines) {
			try {
				StudentRecord record = parseRecord(line);

				database.put(record.getJmbag(), record);
				listOfStudents.add(record);
			}
			catch (IllegalArgumentException | NullPointerException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}
	}

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

	public StudentRecord forJMBAG(String jmbag) {
		return database.get(jmbag);
	}

	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> filtered = new LinkedList<>();

		for (StudentRecord studentRecord : listOfStudents) {
			if (filter.accepts(studentRecord)) {
				filtered.add(studentRecord);
			}
		}

		return filtered;
	}

}
