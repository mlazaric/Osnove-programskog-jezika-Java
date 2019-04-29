package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Command which changes the current working directory and pushes the old directory to the directories stack.
 *
 * @author Marko Lazarić
 */
public class PushdCommand implements ShellCommand {

    public static final String DIRECTORY_STACK_KEY = "cdstack";

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command pushd takes one argument.",
                "It pushes the current working directory to the directory stack and then changes the working directory to the argument."
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
            try {
                pushDirectory(env, args.get(0));
            } catch (IllegalArgumentException e) {
                env.writeln(e.getMessage());
            }
        }
        else {
            env.writeln("Pushd command expects 1 argument, " + args.size() + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    private void pushDirectory(Environment env, String dirName) {
        Path newDir = env.getCurrentDirectory().resolve(dirName);

        if (!Files.isDirectory(newDir)) {
            env.writeln("'" + newDir.toString() + "' is not a valid directory.");
            return;
        }

        Deque<Path> directories = (Deque<Path>) env.getSharedData(DIRECTORY_STACK_KEY);

        if (directories == null) {
            directories = new ArrayDeque<>();

            env.setSharedData(DIRECTORY_STACK_KEY, directories);
        }

        directories.push(env.getCurrentDirectory());
        env.setCurrentDirectory(newDir);
    }

    @Override
    public String getCommandName() {
        return "pushd";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
