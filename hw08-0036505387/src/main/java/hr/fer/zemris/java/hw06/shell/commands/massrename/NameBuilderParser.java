package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple parser for the name builder.
 *
 * @author Marko Lazarić
 */
public class NameBuilderParser {

    /**
     * The parsed name builder.
     */
    private NameBuilder nameBuilder;

    /**
     * Parses the expression into a {@link NameBuilder}.
     *
     * @param expression the expression to parse
     *
     * @throws IllegalArgumentException if the expression is not a valid name builder expression
     */
    public NameBuilderParser(String expression) {
        // Split around ${ and }.
        // "gradovi-${2}-${1,03}.jpg" -> [gradovi-, ${2}, -, ${1,03}, .jpg]
        String[] split = expression.split("((?=\\$\\{)|(?<=}))");

        buildNameBuilder(split);
    }

    /**
     * Parses the split string and builds a {@link CompositeNameBuilder}.
     *
     * @param split the expression split between substitution groups and text
     *
     * @throws IllegalArgumentException if split cannot be parsed
     */
    private void buildNameBuilder(String[] split) {
        List<NameBuilder> nameBuilders = new ArrayList<>();

        for (String part : split) {
            if (part.startsWith("${")) {
                if (!part.endsWith("}")) {
                    throw new IllegalArgumentException("'" + part + "' was not closed");
                }

                String[] arguments = part.substring(2, part.length() - 1).strip().split(",", -1);
                // "${123,332}" -> "123,332" -> ["123", "123"]

                if (arguments.length == 1) {
                    nameBuilders.add(parseSimpleGroup(arguments[0]));
                }
                else if (arguments.length == 2) {
                    nameBuilders.add(parseAdvancedGroup(arguments[0], arguments[1]));
                }
                else {
                    throw new IllegalArgumentException("Too many commas in '" + part + "'.");
                }
            }
            else {
                nameBuilders.add(text(part));
            }

        }

        nameBuilder = new CompositeNameBuilder(nameBuilders.toArray(NameBuilder[]::new));
    }

    /**
     * Parses a group {@link NameBuilder} which has two parameters: index and minWidth with the optional padding.
     *
     * @param index the index of the capturing group
     * @param minWidth the minimum width
     * @return the parsed {@link NameBuilder}
     *
     * @throws IllegalArgumentException if either argument is not a parseable {@link Integer}
     */
    private NameBuilder parseAdvancedGroup(String index, String minWidth) {
        try {
            char padding = ' ';

            if (minWidth.length() > 1 && minWidth.charAt(0) == '0') {
                padding = '0';
            }

            return group(Integer.parseInt(index.strip()), padding, Integer.parseInt(minWidth.strip()));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + index + "' or '" + minWidth + "' is not a valid number.");
        }
    }

    /**
     * Parses a group {@link NameBuilder} which has one parameter.
     *
     * @param index the index of the capturing group
     * @return the parsed {@link NameBuilder}
     *
     * @throws IllegalArgumentException if the argument is not a parseable {@link Integer}
     */
    private NameBuilder parseSimpleGroup(String index) {
        try {
            return group(Integer.parseInt(index.strip()));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + index + "' is not a valid number.");
        }

    }

    /**
     * Returns the parsed {@link NameBuilder}.
     *
     * @return the parsed {@link NameBuilder}
     */
    public NameBuilder getNameBuilder() {
        return nameBuilder;
    }

    /**
     * Creates a {@link NameBuilder} that appends {@code t} to the end.
     *
     * @param t the text to append
     * @return the text {@link NameBuilder}
     */
    static NameBuilder text(String t) {
        return (fr, sb) -> sb.append(t);
    }

    /**
     * Creates a {@link NameBuilder} that prints a captured group.
     *
     * @param index the index of the captured group
     * @return the group {@link NameBuilder}
     */
    static NameBuilder group(int index) {
        return (fr, sb) -> sb.append(fr.group(index));
    }

    /**
     * Creates a {@link NameBuilder} that prints a captured group and pads it on the left with padding character so it
     * is at least minWidth.
     *
     * @param index the index of the captured group
     * @param padding the padding character
     * @param minWidth the minimum width to print
     * @return the advanced group {@link NameBuilder}
     */
    static NameBuilder group(int index, char padding, int minWidth) {
        return (fr, sb) -> sb.append(String.valueOf(padding).repeat(minWidth - fr.group(index).length()))
                             .append(fr.group(index));
    }
}
