package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CdCommand implements ShellCommand {

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
                Path newDir = env.getCurrentDirectory().resolve(args.get(0));

                env.setCurrentDirectory(newDir);
            } catch (IllegalArgumentException e) {
                env.writeln(e.getMessage());
            }
        }
        else {
            env.writeln("Cd command expects 1 argument, " + args.size() + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cd";
    }

    @Override
    public List<String> getCommandDescription() {
        return null;
    }

}
