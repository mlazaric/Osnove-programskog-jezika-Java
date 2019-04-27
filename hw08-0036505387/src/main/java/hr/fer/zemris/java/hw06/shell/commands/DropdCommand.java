package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class DropdCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command dropd takes zero arguments and removes the top directory from the directory stack."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (!arguments.isBlank()) {
            env.writeln("Popd command expects 0 arguments.");
            return ShellStatus.CONTINUE;
        }

        Deque<Path> directories = (Deque<Path>) env.getSharedData(PushdCommand.DIRECTORY_STACK_KEY);

        if (directories == null || directories.isEmpty()) {
            env.writeln("Directory stack is empty.");
        }
        else {
            directories.pop();
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "dropd";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
