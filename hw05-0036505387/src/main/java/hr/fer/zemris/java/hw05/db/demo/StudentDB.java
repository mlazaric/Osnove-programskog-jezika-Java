package hr.fer.zemris.java.hw05.db.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.hw05.db.QueryFilter;
import hr.fer.zemris.java.hw05.db.QueryParser;
import hr.fer.zemris.java.hw05.db.RecordFormatter;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

public class StudentDB {
	public static void main(String[] args) {
		StudentDatabase database = safeParseDatabase(safeReadLinesFromFile());
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("> ");
			String keyWord = sc.next();
			String query = sc.nextLine();

			if (keyWord.equals("query")) {
				try {
					printQueryResults(database, query);
				}
				catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
				catch (Exception e) {
					System.out.println("Invalid query.");
				}
			}
			else if (keyWord.equals("exit")) {
				System.out.println("Goodbye!");
				break;
			}
			else {
				System.out.println("Invalid keyword.");
			}
		}

		sc.close();
	}

	private static void printQueryResults(StudentDatabase database, String query) {
		QueryParser parser = new QueryParser(query);
		List<StudentRecord> records;

		if (parser.isDirectQuery()) {
			System.out.println("Using index for record retrieval.");

			records = new ArrayList<>(1);

			StudentRecord record = database.forJMBAG(parser.getQueriedJMBAG());

			if (record != null) {
				records.add(record);
			}
		}
		else {
			records = database.filter(new QueryFilter(parser.getQuery()));
		}

		RecordFormatter.format(records).forEach(System.out::println);
	}

	private static StudentDatabase safeParseDatabase(List<String> lines) {
		StudentDatabase database = null;

		try {
			database = new StudentDatabase(lines);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		return database;
	}

	private static List<String> safeReadLinesFromFile() {
		List<String> lines = null;

		try {
			lines = Files.readAllLines(Paths.get("./database.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Could not open ./database.txt.");
			System.exit(1);
		}

		return lines;
	}
}

