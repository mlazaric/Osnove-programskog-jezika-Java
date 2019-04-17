package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CopyCommand implements ShellCommand {

    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command copy takes two arguments.",
                "The first argument represents a path to the file to be copied.",
                "The second argument represents where the file should be copied to.",
                "",
                "If the second argument is a directory, the file will be copied to the directory with the current file name.",
                "If the destination file already exists, the user will be prompted whether it should be overwritten or not."
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

        if (args.size() == 2) {
            copyFile(env, args.get(0), args.get(1));
        }
        else {
            env.writeln("Copy command expects 2 arguments, " + args.size() + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    private void copyFile(Environment env, String source, String dest) {
        Path sourcePath = Paths.get(source);
        Path destPath = Paths.get(dest);

        if (Files.isDirectory(sourcePath)) {
            env.writeln("'" + source + "' is not a file.");
            return;
        }

        if (Files.isDirectory(destPath)) {
            copyFile(env, sourcePath, Paths.get(destPath.toString(), sourcePath.getFileName().toString()));
        }
        else {
            copyFile(env, sourcePath, destPath);
        }
    }

    private void copyFile(Environment env, Path sourcePath, Path destPath) {
        if (Files.exists(destPath)) {
            env.write("'" + destPath.toString() + "' already exists. Should I overwrite it? [y/n] ");

            String answer = env.readLine().strip();

            if (!answer.equalsIgnoreCase("y")) {
                env.writeln("File not overwritten.");
                return;
            }
        }

        try (InputStream inputStream = Files.newInputStream(sourcePath);
             OutputStream outputStream = Files.newOutputStream(destPath)) {

            byte[] bytes = new byte[1024];

            while (true) {
                int length = inputStream.read(bytes);

                if (length == -1) {
                    break;
                }

                outputStream.write(bytes, 0, length);
            }

        } catch (IOException e) {
            env.writeln("Error occurred while copying '" + sourcePath.toString() + "' to '" + destPath.toString() + "'.");
        }
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
