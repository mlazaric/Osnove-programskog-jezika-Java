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

/**
 * Command to recursively list the contents of a directory in a tree like format.
 */
public class TreeCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
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

    /**
     * Recursively prints the contents of a directory in a tree like format.
     *
     * @param env the environment in which the command is executed
     * @param dir the directory whose contents should be printed
     */
    private void printTree(Environment env, String dir) {
        Path dirPath = env.getCurrentDirectory().resolve(dir);

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

    /**
     * File visitor for printing the contents of a directory in a tree like format.
     *
     * @author Marko Lazarić
     */
    private static class TreeVisitor implements FileVisitor<Path> {

        /**
         * The current depth within the starting directory. Corresponds to the indentation of the line.
         */
        private int depth = 0;

        /**
         * The environment in which the command was executed. Used for printing.
         */
        private final Environment env;

        /**
         * Creates a new {@link TreeVisitor} with the specified arguments.
         *
         * @param env the environment in which the command was executed
         */
        public TreeVisitor(Environment env) {
            this.env = env;
        }

        /**
         * Prints the name of the directory and increments the depth.
         *
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            env.write("  ".repeat(depth));
            env.writeln(dir.getFileName() == null ? dir.toString() : dir.getFileName().toString());

            ++depth;

            return FileVisitResult.CONTINUE;
        }

        /**
         * Decrements the depth.
         *
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            --depth;

            return FileVisitResult.CONTINUE;
        }

        /**
         * Prints the name of the file.
         *
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            env.write("  ".repeat(depth));
            env.writeln(file.getFileName().toString());

            return FileVisitResult.CONTINUE;
        }

        /**
         * Prints an error message but continues visiting.
         *
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            env.writeln("Failed to visit file '" + file.toString() + "'.");

            return FileVisitResult.CONTINUE;
        }


    }

}
