package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NameBuilderParser {

    private NameBuilder nameBuilder;

    public NameBuilderParser(String expression) {
        // Split around ${ and }.
        // "gradovi-${2}-${1,03}.jpg" -> [gradovi-, ${2}, -, ${1,03}, .jpg]
        String[] split = expression.split("((?=\\$\\{)|(?<=}))");

        buildNameBuilder(split);
    }

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

    private NameBuilder parseAdvancedGroup(String index, String minWidth) {
        try {
            char padding = ' ';

            if (minWidth.length() > 1 && minWidth.charAt(0) == '0') {
                padding = '0';
            }

            return group(Integer.parseInt(index), padding, Integer.parseInt(minWidth));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + index + "' or '" + minWidth + "' is not a valid number.");
        }
    }

    private NameBuilder parseSimpleGroup(String index) {
        try {
            return group(Integer.parseInt(index));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + index + "' is not a valid number.");
        }

    }

    public NameBuilder getNameBuilder() {
        return nameBuilder;
    }

    static NameBuilder text(String t) {
        return (fr, sb) -> sb.append(t);
    }

    static NameBuilder group(int index) {
        return (fr, sb) -> sb.append(fr.group(index));
    }

    static NameBuilder group(int index, char padding, int minWidth) {
        return (fr, sb) -> sb.append(String.valueOf(padding).repeat(minWidth - fr.group(index).length()))
                             .append(fr.group(index));
    }
}
