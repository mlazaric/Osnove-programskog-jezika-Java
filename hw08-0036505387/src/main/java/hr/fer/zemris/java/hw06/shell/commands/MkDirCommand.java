package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Command to create a directory.
 *
 * @author Marko Lazarić
 */
public class MkDirCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command mkdir takes one argument.",
                "The first argument represents a path to a directory to be created.",
                "",
                "Creates a directory from the specified path."
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
            makeDirectory(env, args.get(0));
        }
        else {
            env.writeln("Mkdir command expects 1 argument, " + args.size() + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Creates a directory specified by the dir argument.
     *
     * @param env the environment in which this command is being executed
     * @param dir the directory to be created
     */
    private void makeDirectory(Environment env, String dir) {
        Path dirPath = env.getCurrentDirectory().resolve(dir);

        if (Files.exists(dirPath)) {
            env.writeln("Directory '" + dirPath + "' already exists.");
            return;
        }

        try {
            Files.createDirectory(dirPath);
        } catch (IOException e) {
            env.writeln("Error occurred while trying to create directory '" + dir + "'.");
        }
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
