package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to handle formatting queried records into a tabular, safe to print, form.
 *
 * @author Marko Lazarić
 *
 */
public class RecordFormatter {

	/**
	 * Formats the queried records into a nice table.
	 *
	 * @param records the queried records
	 * @return a list of Strings which are safe to print
	 */
	public static List<String> format(List<StudentRecord> records) {
		if (records == null || records.size() == 0) {
			ArrayList<String> output = new ArrayList<>(1);

			output.add("Records selected: 0");

			return output;
		}

		int lengthOfJmbag = records.stream().map(StudentRecord::getJmbag).mapToInt(String::length).max().getAsInt();
		int lengthOfFirstName = records.stream().map(StudentRecord::getFirstName).mapToInt(String::length).max().getAsInt();
		int lengthOfLastName = records.stream().map(StudentRecord::getLastName).mapToInt(String::length).max().getAsInt();
		int lengthOfGrade = 1; // Always 1

		String topAndBottomBorder = "+" + repeat("=", lengthOfJmbag + 2) + "+" + repeat("=", lengthOfLastName + 2) + "+" +
				repeat("=", lengthOfFirstName + 2) + "+" + repeat("=", lengthOfGrade + 2) + "+";

		List<String> output = new ArrayList<>();

		output.add(topAndBottomBorder);

		records.forEach(record -> {
			String formatString = "| %-" + lengthOfJmbag + "s | %-" + lengthOfLastName + "s | %-" + lengthOfFirstName + "s | %d |";

			output.add(String.format(formatString, record.getJmbag(), record.getLastName(), record.getFirstName(), record.getFinalGrade()));
		});

		output.add(topAndBottomBorder);

		output.add("Records selected: " + records.size());

		return output;
	}

	/**
	 * Repeats the string n number of times.
	 *
	 * @param toRepeat the string to repeat
	 * @param times the number of times to repeat
	 * @return the repeated string
	 */
	private static String repeat(String toRepeat, int times) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < times; i++) {
			sb.append(toRepeat);
		}

		return sb.toString();
	}

}
