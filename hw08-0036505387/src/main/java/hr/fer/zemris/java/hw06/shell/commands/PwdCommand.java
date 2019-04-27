package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.List;

public class PwdCommand implements ShellCommand {

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
        return null;
    }

}
