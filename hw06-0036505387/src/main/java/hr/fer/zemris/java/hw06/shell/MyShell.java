package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.commands.*;

import java.util.*;

/**
 * A simple shell emulator.
 *
 * @author Marko Lazarić
 */
public class MyShell {

    /**
     * The name of the shell.
     */
    private static final String NAME = "MyShell";

    /**
     * The version of the shell.
     */
    private static final String VERSION = "1.0";

    /**
     * The welcome message for the shell.
     */
    private static final String WELCOME_MESSAGE = "Welcome to " + NAME + " v " + VERSION;

    /**
     * Emulates a shell. Continually prompts the user for commands and executes those commands.
     *
     * If a command encounters an error, a relevant message is printed and the user is prompted for the next command.
     *
     * To input commands spanning multiple lines, each line (except the last) should be suffixed with the {@link ShellEnvironment#morelineSymbol}.
     * To exit the shell, use the "exit" command.
     * To list commands and get a description of a command, use the "help" command.
     *
     * @param args the arguments are ignored.
     */
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Environment environment = new ShellEnvironment(sc);
            Map<String, ShellCommand> commands = environment.commands();

            environment.writeln(WELCOME_MESSAGE);

            while (true) {
                String line = readLine(environment);

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

    /**
     * Executes a {@link ShellCommand} with a cleaned up {@link String} containing only the arguments to the command,
     * and not the command name.
     *
     * @param command the command to execute
     * @param environment the environment to use for execution
     * @param line the line containing the whole command
     * @return the status returned by the command
     */
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

    /**
     * Returns the command invoked in the line.
     *
     * @param commands the mapping of command names to command objects
     * @param line the line inputted by the user
     * @return the command invoked in the line
     */
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

    /**
     * Reads a command spanning one or more rows into a {@link String} without any new lines.
     *
     * @param environment the environment used for reading lines
     * @return a string containing the command and its arguments
     */
    private static String readLine(Environment environment) {
        environment.write(environment.getPromptSymbol() + " ");

        StringBuilder sb = new StringBuilder();

        while (true) {
            String line = environment.readLine();

            sb.append(line);

            if (line.isBlank()) {
                break;
            }

            if (line.charAt(line.length() - 1) != environment.getMorelinesSymbol()) {
                break;
            }

            // Delete MORELINES symbol
            sb.deleteCharAt(sb.length() - 1);

            environment.write(environment.getMultilineSymbol() + " ");
        }

        return sb.toString().strip();
    }

    /**
     * Models the environment of a shell which writes to System.out and reads from System.in.
     *
     * @author Marko Lazarić
     */
    private static class ShellEnvironment implements Environment {

        /**
         * An instance of {@link Scanner} used for reading lines.
         */
        private Scanner scanner;

        /**
         * A mapping of command names to command objects.
         */
        private SortedMap<String, ShellCommand> commands;

        /**
         * Multiline symbol - printed when prompting the user for the next line of a command, if the previous line
         * ends with more lines symbol.
         */
        private char multilineSymbol = '|';

        /**
         * Prompt symbol - printed when prompting the user for the first line of a command.
         */
        private char promptSymbol = '>';

        /**
         * More line symbol - indicates a command spanning multiple rows. Must appear at the end, if present.
         */
        private char morelineSymbol = '\\';

        /**
         * Creates a new {@link ShellEnvironment} with the given {@link Scanner} instance.
         *
         * @param scanner the scanner used for user input
         */
        private ShellEnvironment(Scanner scanner) {
            this.scanner = scanner;
            this.commands = new TreeMap<>();

            loadCommands();
        }

        /**
         * Loads all the available commands into {@link #commands}.
         */
        private void loadCommands() {
            commands.put("symbol", new SymbolCommand());
            commands.put("exit", new ExitCommand());
            commands.put("charsets", new CharsetsCommand());
            commands.put("cat", new CatCommand());
            commands.put("help", new HelpCommand());
            commands.put("ls", new LsCommand());
            commands.put("tree", new TreeCommand());
            commands.put("copy", new CopyCommand());
            commands.put("mkdir", new MkDirCommand());
            commands.put("hexdump", new HexDumpCommand());
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
