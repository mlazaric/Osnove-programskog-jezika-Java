package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelpCommand implements ShellCommand {

    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command help takes one or zero arguments.",
                "If zero arguments are given, it will list the available commands.",
                "If one argument is given representing the name of a command, it will print that command's description."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (arguments.isBlank()) {
            for (ShellCommand command : env.commands().values()) {
                env.writeln(command.getCommandName());
            }
        }
        else {
            ShellCommand command = env.commands().get(arguments);

            if (command == null) {
                env.writeln("'" + arguments + "' is not a valid command name.");

                return ShellStatus.CONTINUE;
            }

            command.getCommandDescription().stream().forEach(env::writeln);
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
