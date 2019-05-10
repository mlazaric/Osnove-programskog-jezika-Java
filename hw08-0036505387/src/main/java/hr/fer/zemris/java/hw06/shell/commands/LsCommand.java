package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Command to list the files and directories in the current directory.
 *
 * @author Marko Lazarić
 */
public class LsCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command ls takes one argument.",
                "The first argument represents a path to a directory.",
                "",
                "Prints the contents of the directory.",
                "Each line represents one file or directory within the specified directory."
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
            listContents(env, args.get(0));
        }
        else {
            env.writeln("Ls command expects 1 argument, " + args.size() + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Prints the contents of the specified dir to the environment's output stream.
     *
     * @param env the environment in which this command was executed
     * @param dir the specified directory whose contents should be printed
     */
    private void listContents(Environment env, String dir) {
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
            Files.list(dirPath).forEach(p -> printLine(env, p));
        } catch (IOException e) {
            env.writeln("Error occurred while reading the contents of '" + dir + "'.");
        } catch (RuntimeException e) {
            env.writeln("Error occurred while reading attributes of a file.");
        }
    }

    /**
     * Prints a single line representing either a file or a directory within the specified directory.
     *
     * @param env the environment in which this command was executed
     * @param path the file or directory represented by this line
     */
    private void printLine(Environment env, Path path) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BasicFileAttributeView faView = Files.getFileAttributeView(
                path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );
        BasicFileAttributes attributes = null;

        try {
            attributes = faView.readAttributes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileTime fileTime = attributes.creationTime();
        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

        env.write(attributes.isDirectory() ? "d" : "-");
        env.write(Files.isReadable(path) ? "r" : "-");
        env.write(Files.isWritable(path) ? "w" : "-");
        env.write(Files.isExecutable(path) ? "x" : "-");
        env.write(" ");

        String size = String.valueOf(attributes.size());

        // Size column is right aligned and occupies 10 characters
        env.write(" ".repeat(10 - size.length()));
        env.write(size);
        env.write(" ");

        env.write(formattedDateTime);
        env.write(" ");

        env.writeln(path.getFileName().toString());

    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
