package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.file.Path;
import java.util.Deque;
import java.util.List;

public class PopdCommand implements ShellCommand {

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
        return null;
    }

}
