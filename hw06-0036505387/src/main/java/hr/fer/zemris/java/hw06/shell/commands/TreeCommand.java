package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TreeCommand implements ShellCommand {

    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command tree takes one argument.",
                "The first argument represents a path to a directory.",
                "",
                "Recursively prints the contents of the directory.",
                "Each line represents one file or directory within the specified directory.",
                "The indentation of the line corresponds to the depth of that file or directory within the specified directory."
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
            printTree(env, args.get(0));
        }
        else {
            env.writeln("Tree command expects 1 argument, " + args.size() + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    private void printTree(Environment env, String dir) {
        Path dirPath = Paths.get(dir);

        if (!Files.exists(dirPath)) {
            env.writeln("Directory '" + dir + "' does not exist.");
            return;
        }

        if (!Files.isDirectory(dirPath)) {
            env.writeln("'" + dir + "' is not a directory.");
            return;
        }

        try {
            Files.walkFileTree(dirPath, new TreeVisitor(env));
        } catch (IOException e) {
            env.writeln("Error occurred while recursively listing contents of '" + dir + "'.");
        }

    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

    private static class TreeVisitor implements FileVisitor<Path> {
        private int depth = 0;
        private final Environment env;

        public TreeVisitor(Environment env) {
            this.env = env;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            env.write("  ".repeat(depth));
            env.writeln(dir.getFileName().toString());

            ++depth;

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            --depth;

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            env.write("  ".repeat(depth));
            env.writeln(file.getFileName().toString());

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            env.writeln("Failed to visit file '" + file.toString() + "'.");

            return FileVisitResult.CONTINUE;
        }


    }

}
