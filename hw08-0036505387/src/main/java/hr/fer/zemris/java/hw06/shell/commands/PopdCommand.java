package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Command which sets the current working directory to the top entry on the directories stack.
 *
 * @author Marko Lazarić
 */
public class PopdCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command popd takes zero arguments.",
                "It pops the top directory from the directory stack and changes the working directory to it."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (!arguments.isBlank()) {
            env.writeln("Popd command expects 0 arguments.");
            return ShellStatus.CONTINUE;
        }

        try {
            popDirectory(env);
        }
        catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
        }

        return ShellStatus.CONTINUE;
    }

    private void popDirectory(Environment env) {
        Deque<Path> directories = (Deque<Path>) env.getSharedData(PushdCommand.DIRECTORY_STACK_KEY);

        if (directories == null || directories.isEmpty()) {
            throw new IllegalArgumentException("Directory stack is empty.");
        }

        Path newDir = directories.pop();

        env.setCurrentDirectory(newDir);
        env.writeln("Current directory changed to '" + env.getCurrentDirectory().toString() + "'.");
    }

    @Override
    public String getCommandName() {
        return "popd";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
