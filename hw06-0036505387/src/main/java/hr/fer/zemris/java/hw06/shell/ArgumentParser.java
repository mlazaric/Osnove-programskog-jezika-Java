package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for the command arguments.
 *
 * @author Marko Lazarić
 */
public class ArgumentParser {

    /**
     * The input data to parse as arguments.
     */
    private final char[] data;

    /**
     * The next index to parse.
     */
    private int currentIndex = 0;

    /**
     * A list of the parsed arguments.
     */
    private final List<String> arguments;

    /**
     * Creates a new {@link ArgumentParser} and parses the given {@link String}.
     *
     * @param input the text to parse as command arguments
     */
    public ArgumentParser(String input) {
        this.data = input.toCharArray();
        this.arguments = new ArrayList<>();

        parse();
    }

    /**
     * Parses the input text into a list of command arguments.
     */
    private void parse() {
        while (isValidIndex()) {
            skipWhitespace();

            if (!isValidIndex()) {
                break;
            }

            if (data[currentIndex] == '"') {
                arguments.add(readString());
            }
            else {
                arguments.add(readWord());
            }
        }
    }

    /**
     * Reads a word argument.
     *
     * Stops reading the word after it encounters a whitespace or it reaches the end of input text.
     * The word argument can contain any characters except quotation marks.
     *
     * @return the word argument
     */
    private String readWord() {
        int beginIndex = currentIndex;
        int length = 0;

        while (isValidIndex() && !Character.isWhitespace(data[currentIndex])) {
            ++currentIndex;
            ++length;
        }

        return new String(data, beginIndex, length);
    }

    /**
     * Reads a string argument.
     *
     * Starts reading from the first quotation mark and stops reading when it encounters a second one.
     * \" is escaped to " and \\ is escaped to \. Everything else is copied literally.
     *
     * @return the string argument
     *
     * @throws IllegalArgumentException if the string was not closed or there is a non whitespace character after the
     *                                  quotation mark
     */
    private String readString() {
        StringBuilder sb = new StringBuilder();

        ++currentIndex;

        while (isValidIndex()) {
            if (data[currentIndex] == '"') {
                break;
            }
            else if (data[currentIndex] == '\\') {
                ++currentIndex;

                if (isValidIndex()) {
                    // If the backslash is followed by a quotation mark or another backslash,
                    // ignore the leading backslash.
                    // Any other escaping sequence should be left as is.
                    if (!(data[currentIndex] == '"' || data[currentIndex] == '\\')) {
                        sb.append('\\');
                    }
                }
                else { // If it is at the end, just append it as is.
                    sb.append('\\');
                    break;
                }
            }

            sb.append(data[currentIndex]);
            ++currentIndex;
        }

        if (!isValidIndex()) {
            throw new IllegalArgumentException("String was not closed.");
        }

        ++currentIndex;

        if (isValidIndex() && !Character.isWhitespace(data[currentIndex])) {
            throw new IllegalArgumentException("String should be followed by at least one space.");
        }

        return sb.toString();
    }

    /**
     * Skips all whitespace characters.
     */
    private void skipWhitespace() {
        while (isValidIndex() && Character.isWhitespace(data[currentIndex])) {
            ++currentIndex;
        }
    }

    /**
     * Returns whether {@link #currentIndex} is a valid index.
     *
     * @return whether {@link #currentIndex} is a valid index
     */
    private boolean isValidIndex() {
        return currentIndex < data.length;
    }

    /**
     * Returns the parsed arguments.
     *
     * @return the parsed arguments
     */
    public List<String> getArguments() {
        return arguments;
    }
}
