package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Command which prints the current working directory.
 *
 * @author Marko Lazarić
 */
public class PwdCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command pwd takes zero arguments and prints the current working directory."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (!arguments.isBlank()) {
            env.writeln("Pwd command expects 0 arguments.");
            return ShellStatus.CONTINUE;
        }

        env.writeln(env.getCurrentDirectory().toString());

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "pwd";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
