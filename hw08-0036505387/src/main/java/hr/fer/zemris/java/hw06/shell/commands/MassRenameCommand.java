package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class MassRenameCommand implements ShellCommand {

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
                Path path = env.getCurrentDirectory().resolve(args.get(0));
                List<FilterResult> filtered = filter(path, args.get(3));

                filtered.forEach(f -> env.writeln(f.toString()));
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
                    .filter(Files::isRegularFile) // Only files should be considered.
                    .map(Path::getFileName)
                    .map(Path::toString) // Map to file name.
                    .map(pattern::matcher) // Create Matchers.
                    .filter(Matcher::matches) // Remove files that do not match tha pattern.
                    .map(FilterResult::new) // Create FilterResults.
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

    private static class FilterResult {
        private final Matcher matcher;

        private FilterResult(Matcher matcher) {
            this.matcher = matcher;
        }

        @Override
        public String toString() {
            return matcher.group(0);
        }

        public int numberOfGroups() {
            return matcher.groupCount();
        }

        public String group(int index) {
            return matcher.group(index);
        }
    }
}
