package hr.fer.zemris.lsystems.impl;

import java.awt.*;

/**
 * Helper methods for parsing configuration and commands.
 *
 * @author Marko Lazarić
 *
 * @see CommandParser
 * @see ConfigurationParser
 */
public final class ParserUtil {

    /**
     * This class should not be instanced.
     */
    private ParserUtil() {}

    /**
     * Parses a double from string or throws {@link IllegalArgumentException}.
     *
     * @param number the string to parse as double
     * @return the parsed double
     *
     * @throws IllegalArgumentException if the string does not represent a valid double
     */
    public static double parseDoubleOrThrow(String number) {
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
    public static void throwIfWrongNumberOfArguments(int expectedNumberOfArguments, int realNumberOfArguments) {
        if (expectedNumberOfArguments != realNumberOfArguments) {
            throw new IllegalArgumentException("Invalid number of arguments passed, expected " +
                    expectedNumberOfArguments + ", got " + realNumberOfArguments + ".");
        }
    }

    /**
     * Parses a {@link Color} from the string or throws {@link IllegalArgumentException}.
     *
     * @param color the string to parse as a {@link Color}
     * @return the parsed {@link Color}
     *
     * @throws IllegalArgumentException if the string does not represent a valid color
     */
    public static Color parseColorOrThrow(String color) {
        try {
            return Color.decode("#" + color);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + color + "' is not a valid color.");
        }
    }

}
