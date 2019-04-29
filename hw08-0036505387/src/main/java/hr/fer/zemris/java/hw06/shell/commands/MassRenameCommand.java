package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.massrename.FilterResult;
import hr.fer.zemris.java.hw06.shell.commands.massrename.NameBuilder;
import hr.fer.zemris.java.hw06.shell.commands.massrename.NameBuilderParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class MassRenameCommand implements ShellCommand {

    private static final Map<String, SubCommand> SUBCOMMANDS;

    static {
        SUBCOMMANDS = new HashMap<>();

        SUBCOMMANDS.put("filter", MassRenameCommand::filterSubCommand);
        SUBCOMMANDS.put("groups", MassRenameCommand::groupsSubCommand);
        SUBCOMMANDS.put("show", MassRenameCommand::showSubCommand);
        SUBCOMMANDS.put("execute", MassRenameCommand::executeSubCommand);
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

        if (args.size() < 4) {
            env.writeln("Massrename command expects at least 4 arguments.");
        }
        else {
            try {
                Path dir1 = env.getCurrentDirectory().resolve(args.get(0));
                Path dir2 = env.getCurrentDirectory().resolve(args.get(1));

                if (!Files.isDirectory(dir1)) {
                    env.writeln("'" + args.get(0) + "' is not a directory.");
                    return ShellStatus.CONTINUE;
                }
                if (!Files.isDirectory(dir2)) {
                    env.writeln("'" + args.get(1) + "' is not a directory.");
                    return ShellStatus.CONTINUE;
                }

                List<FilterResult> filtered = filter(dir1, args.get(3));

                handleSubCommand(env, dir1, dir2, args.subList(2, args.size()), filtered);
            }
            catch (IllegalArgumentException e) {
                env.writeln(e.getMessage());
            }
            catch (IOException e) {
                env.writeln("IO error occurred while executing command.");
            }
        }

        return ShellStatus.CONTINUE;
    }

    private void handleSubCommand(Environment env, Path dir1, Path dir2, List<String> args, List<FilterResult> results) {
        SubCommand subCommand = SUBCOMMANDS.get(args.get(0));

        if (subCommand == null) {
            env.writeln("Invalid subcommand '" + args.get(0) + "'.");
        }
        else {
            try {
                subCommand.executeSubCommand(env, dir1, dir2, args, results);
            }
            catch (IllegalArgumentException e) {
                env.writeln(e.getMessage());
            }
        }
    }

    private List<FilterResult> filter(Path dir, String regex) throws IOException {
        Pattern pattern = null;

        try {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        }
        catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Invalid regex.");
        }

        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException("'" + dir.toString() + "' is not a directory.");
        }

        return Files.list(dir)
                    .filter(Files::isRegularFile)       // Only files should be considered.
                    .map(Path::getFileName)
                    .map(Path::toString)                // Map to file name.
                    .map(pattern::matcher)              // Create Matchers.
                    .filter(Matcher::matches)           // Remove files that do not match tha pattern.
                    .map(FilterResult::new)             // Create FilterResults.
                    .collect(Collectors.toList());


    }

    @Override
    public String getCommandName() {
        return "massrename";
    }

    @Override
    public List<String> getCommandDescription() {
        return null;
    }

    @FunctionalInterface
    private interface SubCommand {

        void executeSubCommand(Environment env, Path dir1, Path dir2, List<String> args, List<FilterResult> results);

    }

    private static void filterSubCommand(Environment env, Path dir1, Path dir2, List<String> args, List<FilterResult> results) {
        if (args.size() != 2) { // First argument is the command name, the second is the regex
            env.writeln("'filter' expects exactly 1 argument.");
            return;
        }

        results.forEach(r -> env.writeln(r.toString()));
    }

    private static void groupsSubCommand(Environment env, Path dir1, Path dir2, List<String> args, List<FilterResult> results) {
        if (args.size() != 2) { // First argument is the command name, the second is the regex
            env.writeln("'groups' expects exactly 1 argument.");
            return;
        }

        for (FilterResult result : results) {
            env.write(result.toString());
            env.write(" ");

            int length = result.numberOfGroups();

            for (int index = 0; index <= length; ++index) {
                env.write(index + ": ");
                env.write(result.group(index));
                env.write(" ");
            }

            env.writeln("");
        }
    }

    private static void showSubCommand(Environment env, Path dir1, Path dir2, List<String> args, List<FilterResult> results) {
        if (args.size() != 3) { // First argument is the command name, the second is the regex and the third is the name builder pattern
            env.writeln("'show' expects exactly 2 arguments.");
            return;
        }

        NameBuilder nameBuilder = null;

        try {
            nameBuilder = new NameBuilderParser(args.get(2)).getNameBuilder();
        }
        catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (FilterResult result : results) {
            sb.append(result.toString());
            sb.append(" => ");

            nameBuilder.execute(result, sb);

            sb.append('\n');
        }

        env.writeln(sb.toString());
    }

    private static void executeSubCommand(Environment env, Path dir1, Path dir2, List<String> args, List<FilterResult> results) {
        if (args.size() != 3) { // First argument is the command name, the second is the regex and the third is the name builder pattern
            env.writeln("'execute' expects exactly 2 arguments.");
            return;
        }

        NameBuilder nameBuilder = null;

        try {
            nameBuilder = new NameBuilderParser(args.get(2)).getNameBuilder();
        }
        catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return;
        }

        for (FilterResult result : results) {
            String oldName = result.toString();

            StringBuilder sb = new StringBuilder();
            nameBuilder.execute(result, sb);
            String newName = sb.toString();

            Path from = dir1.resolve(oldName);
            Path to = dir2.resolve(newName);

            if (!Files.exists(from)) {
                throw new IllegalArgumentException("'" + from.toString() + "' does not exist.");
            }
            if (Files.exists(to)) {
                throw new IllegalArgumentException("'" + to.toString() + "' already exists. Terminating execution.");
            }

            try {
                Files.move(from, to);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
