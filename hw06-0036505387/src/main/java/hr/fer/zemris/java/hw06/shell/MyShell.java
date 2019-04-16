package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.commands.*;

import java.util.*;

public class MyShell {

    private static final String NAME = "MyShell";
    private static final String VERSION = "1.0";
    private static final String WELCOME_MESSAGE = "Welcome to " + NAME + " v " + VERSION;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Environment environment = new ShellEnvironment(sc);
            Map<String, ShellCommand> commands = environment.commands();

            environment.writeln(WELCOME_MESSAGE);

            while (true) {
                String line = readLine(environment, sc);

                if (line.isBlank()) {
                    continue;
                }

                ShellCommand command = getCommandFromLine(commands, line);

                if (command == null) {
                    environment.writeln("'" + line + "' is not a valid command.");
                    continue;
                }

                if (executeCommand(command, environment, line) == ShellStatus.TERMINATE) {
                    break;
                }
            }
        }
        catch (ShellIOException e) {
            System.err.println("Unrecoverable error encountered. Terminating program.");
            System.exit(1);
        }
    }

    private static ShellStatus executeCommand(ShellCommand command, Environment environment, String line) {
        int indexOfFirstSpace = line.indexOf(' ');
        String arguments;

        if (indexOfFirstSpace == -1) { // Command with no arguments
            arguments = "";
        }
        else { // Remove command name and strip any possible trailing or leading spaces
            arguments = line.substring(indexOfFirstSpace).strip();
        }

        return command.executeCommand(environment, arguments);
    }

    private static ShellCommand getCommandFromLine(Map<String, ShellCommand> commands, String line) {
        int indexOfFirstSpace = line.indexOf(' ');
        String commandName;

        if (indexOfFirstSpace == -1) { // If it is a command with no arguments, it will not have a space
                                       // so the whole line is the command name.
            commandName = line;
        }
        else { // Otherwise get the first word in the line and use it as the command name.
            commandName = line.substring(0, indexOfFirstSpace);
        }

        return commands.get(commandName);
    }

    private static String readLine(Environment environment, Scanner sc) {
        environment.write(environment.getPromptSymbol() + " ");

        StringBuilder sb = new StringBuilder();

        while (true) {
            String line = sc.nextLine();

            sb.append(line);

            if (line.charAt(line.length() - 1) != environment.getMorelinesSymbol()) {
                break;
            }

            // Delete MORELINES symbol
            sb.deleteCharAt(sb.length() - 1);

            environment.write(environment.getMultilineSymbol() + " ");
        }

        return sb.toString().strip();
    }

    private static class ShellEnvironment implements Environment {

        private Scanner scanner;
        private SortedMap<String, ShellCommand> commands;
        private char multilineSymbol = '|';
        private char promptSymbol = '>';
        private char morelineSymbol = '\\';

        private ShellEnvironment(Scanner scanner) {
            this.scanner = scanner;
            this.commands = new TreeMap<>();

            loadCommands();
        }

        private void loadCommands() {
            commands.put("symbol", new SymbolCommand());
            commands.put("exit", new ExitCommand());
            commands.put("charsets", new CharsetsCommand());
            commands.put("cat", new CatCommand());
            commands.put("help", new HelpCommand());
        }

        @Override
        public String readLine() throws ShellIOException {
            try {
                return scanner.nextLine();
            } catch (Exception e) {
                throw new ShellIOException(e);
            }
        }

        @Override
        public void write(String text) throws ShellIOException {
            try {
                System.out.print(text);
            } catch (Exception e) {
                throw new ShellIOException(e);
            }
        }

        @Override
        public void writeln(String text) throws ShellIOException {
            try {
                System.out.println(text);
            } catch (Exception e) {
                throw new ShellIOException(e);
            }
        }

        @Override
        public SortedMap<String, ShellCommand> commands() {
            return Collections.unmodifiableSortedMap(commands);
        }

        @Override
        public Character getMultilineSymbol() {
            return multilineSymbol;
        }

        @Override
        public void setMultilineSymbol(Character symbol) {
            multilineSymbol = symbol;
        }

        @Override
        public Character getPromptSymbol() {
            return promptSymbol;
        }

        @Override
        public void setPromptSymbol(Character symbol) {
            promptSymbol = symbol;
        }

        @Override
        public Character getMorelinesSymbol() {
            return morelineSymbol;
        }

        @Override
        public void setMorelinesSymbol(Character symbol) {
            morelineSymbol = symbol;
        }
    }
}
