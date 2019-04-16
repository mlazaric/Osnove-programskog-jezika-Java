package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {

    private final char[] data;
    private int currentIndex = 0;
    private final List<String> arguments;

    public ArgumentParser(String input) {
        this.data = input.toCharArray();
        this.arguments = new ArrayList<>();

        parse();
    }

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

    private String readWord() {
        int beginIndex = currentIndex;
        int length = 0;

        while (isValidIndex() && !Character.isWhitespace(data[currentIndex])) {
            ++currentIndex;
            ++length;
        }

        return new String(data, beginIndex, length);
    }

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


    private void skipWhitespace() {
        while (isValidIndex() && Character.isWhitespace(data[currentIndex])) {
            ++currentIndex;
        }
    }

    private boolean isValidIndex() {
        return currentIndex < data.length;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
