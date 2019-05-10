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
 * Command which lists the contents of the directories stack.
 *
 * @author Marko Lazarić
 */
public class ListdCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command popd takes zero arguments and lists the contents of the directory stack."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (!arguments.isBlank()) {
            env.writeln("Listd command expects 0 arguments.");
            return ShellStatus.CONTINUE;
        }

        Deque<Path> directories = (Deque<Path>) env.getSharedData(PushdCommand.DIRECTORY_STACK_KEY);

        if (directories == null || directories.isEmpty()) {
            env.writeln("Directory stack is empty.");
        }
        else {
            directories.forEach(p -> env.writeln(p.toString()));
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "listd";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
