package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HexDumpCommand implements ShellCommand {

    private static final int MIN_PRINT_CHARACTER = 32;
    private static final int MAX_PRINT_CHARACTER = 127;
    private static final int NUMBER_OF_BYTES_PER_LINE = 16;
    private static final int LINE_NUMBER_LENGTH = 8;
    private static final int BYTES_PER_GROUP = 8;

    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command hexdump takes one argument.",
                "The first argument is a path to the file which is read.",
                "",
                "Reads the contents of the file and prints the hexadecimal representation of its contents."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args;

        try {
            args = new ArgumentParser(arguments).getArguments();
        }
        catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());

            return ShellStatus.CONTINUE;
        }

        if (args.size() == 1) {
            hexDump(env, args.get(0));
        }
        else {
            env.writeln("Hexdump command expects 1 argument, " + args.size() + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    private void hexDump(Environment env, String file) {
        Path filePath = Paths.get(file);

        if (!Files.exists(filePath)) {
            env.writeln("File '" + file + "' does not exist.");
            return;
        }

        if (!Files.isReadable(filePath)) {
            env.writeln("File '" + file + "' is not readable.");
            return;
        }

        if (Files.isDirectory(filePath)) {
            env.writeln("'" + file + "' is a directory.");
            return;
        }

        hexDump(env, filePath);
    }

    private void hexDump(Environment env, Path filePath) {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            long lineNumber = 0;
            byte[] bytes = new byte[NUMBER_OF_BYTES_PER_LINE];

            while (true) {
                int length = inputStream.read(bytes);

                if (length == -1) {
                    break;
                }

                String lineNumberString = Long.toString(lineNumber, 16);

                env.write("0".repeat(LINE_NUMBER_LENGTH - lineNumberString.length()));
                env.write(lineNumberString);
                env.write(": ");

                writeBytes(env, bytes, length);
                env.write("| ");
                writeCharacters(env, bytes, length);

                env.writeln("");

                lineNumber += NUMBER_OF_BYTES_PER_LINE;
            }

        }
        catch (IOException e) {
            env.writeln("Error occurred while reading from file '" + filePath.toString() + "'.");
        }

    }

    private void writeCharacters(Environment env, byte[] bytes, int length) {
        for (int column = 0; column < length; ++column) {
            if (bytes[column] < MIN_PRINT_CHARACTER || bytes[column] > MAX_PRINT_CHARACTER) {
                env.write(".");
            }
            else {
                env.write(String.valueOf((char) bytes[column]));
            }
        }
    }

    private void writeBytes(Environment env, byte[] bytes, int length) {
        for (int column = 0; column < NUMBER_OF_BYTES_PER_LINE; ++column) {
            if (column >= length) {
                env.write("  ");
            }
            else {
                env.write(String.format("%02x", bytes[column]));
            }

            // Do not print a | after the last byte.
            // Otherwise print a | after every BYTES_PER_GROUP of bytes printed.
            if (((column + 1) % BYTES_PER_GROUP) == 0 && column != (NUMBER_OF_BYTES_PER_LINE - 1)) {
                env.write("|");
            }
            else {
                env.write(" ");
            }
        }
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
