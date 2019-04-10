package hr.fer.zemris.lsystems.impl;

import java.util.Objects;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Helper class for parsing configuration strings.
 *
 * @author Marko Lazarić
 *
 */
public final class ConfigurationParser {

	/**
	 * This class should not be instanced.
	 */
	private ConfigurationParser() {}

	/**
	 * Parse a configuration line.
	 *
	 * @param line the line to parse
	 * @param builder the builder to configure
	 *
	 * @throws IllegalArgumentException if it is not a valid configuration line
	 * @throws NullPointerException if either argument is {@code null}
	 */
	public static void parse(String line, LSystemBuilder builder) {
		Objects.requireNonNull(line, "Cannot parse a null line.");
		Objects.requireNonNull(builder, "Cannot configure a null builder.");
		
		if (line.isBlank()) {
			return;
		}

		String[] parts = line.strip().split("\\s+");
		int numberOfArguments = parts.length - 1;

		switch (parts[0]) {
			case "origin":
				throwIfWrongNumberOfArguments(2, numberOfArguments);
				builder.setOrigin(parseDoubleOrThrow(parts[1]), parseDoubleOrThrow(parts[2]));
				break;
	
			case "angle":
				throwIfWrongNumberOfArguments(1, numberOfArguments);
				builder.setAngle(parseDoubleOrThrow(parts[1]));
				break;
	
			case "unitLength":
				throwIfWrongNumberOfArguments(1, numberOfArguments);
				builder.setUnitLength(parseDoubleOrThrow(parts[1]));
				break;
	
			case "unitLengthDegreeScaler":
				parseUnitLengthDegreeScaler(line, builder);
				break;
	
			case "command":
				// command F draw 1
				// 0123456789012345
				// 0         1
				builder.registerCommand(parts[1].charAt(0), line.substring(10));
				break;
	
			case "axiom":
				throwIfWrongNumberOfArguments(1, numberOfArguments);
				builder.setAxiom(parts[1]);
				break;
	
			case "production":
				throwIfWrongNumberOfArguments(2, numberOfArguments);
				builder.registerProduction(parts[1].charAt(0), parts[2]);
				break;
	
			default:
				throw new IllegalArgumentException("'" + line + "' is not a valid configuration line.");
		}
	}

	/**
	 * Parses a line configuring unitLengthDegreeScaler.
	 *
	 * @param line the line to parse
	 * @param builder the builder to configure
	 *
	 * @throws IllegalArgumentException if it is not a valid unitLengthDegreeScaler configuration line
	 */
	private static void parseUnitLengthDegreeScaler(String line, LSystemBuilder builder) {
		if (line.contains("/")) {
			// unitLengthDegreeScaler 1.0 / 3.0
			// 012345678901234567890123456789012
			// 0         1         2         3
			String[] parts = line.substring(23).strip().split("\\s*/\\s*");

			throwIfWrongNumberOfArguments(2, parts.length);

			builder.setUnitLengthDegreeScaler(parseDoubleOrThrow(parts[0]) / parseDoubleOrThrow(parts[1]));
		}
		else {
			String[] parts = line.strip().split("\\s+");

			throwIfWrongNumberOfArguments(1, parts.length - 1);

			builder.setUnitLengthDegreeScaler(parseDoubleOrThrow(parts[1]));
		}
	}

	/**
	 * Parses a double from string or throws {@link IllegalArgumentException}.
	 *
	 * @param number the string to parse as double
	 * @return the parsed double
	 *
	 * @throws IllegalArgumentException if the string does not represent a valid double
	 */
	private static double parseDoubleOrThrow(String number) {
		try {
			return Double.parseDouble(number);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("'" + number + "' is not a valid double.");
		}
	}

	/**
	 * Checks expected and real number of arguments, if they are not equal, it throws an {@link IllegalArgumentException}.
	 *
	 * @param expectedNumberOfArguments the expected number of arguments
	 * @param realNumberOfArguments the real number of arguments
	 *
	 * @throws IllegalArgumentException if they are not equal
	 */
	private static void throwIfWrongNumberOfArguments(int expectedNumberOfArguments, int realNumberOfArguments) {
		if (expectedNumberOfArguments != realNumberOfArguments) {
			throw new IllegalArgumentException("Invalid number of arguments passed, expected " +
					expectedNumberOfArguments + ", got " + realNumberOfArguments + ".");
		}
	}

}
