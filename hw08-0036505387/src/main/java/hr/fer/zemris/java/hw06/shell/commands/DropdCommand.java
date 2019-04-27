package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.file.Path;
import java.util.Deque;
import java.util.List;

public class DropdCommand implements ShellCommand {

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
        return null;
    }

}
