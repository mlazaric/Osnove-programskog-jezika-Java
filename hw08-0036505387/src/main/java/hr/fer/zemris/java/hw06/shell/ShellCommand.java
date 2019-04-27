package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Models a simple shell command.
 *
 * @author Marko Lazarić
 */
public interface ShellCommand {

    /**
     * Executes the command. The command should handle the argument parsing and should catch any exceptions that get
     * thrown while executing the command.
     *
     * @param env the environment in which to execute the command
     * @param arguments the arguments to the command
     * @return the status of the shell after the command
     *
     * @see ShellStatus
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Returns the name of the exception.
     *
     * @return the name of the exception.
     *
     * @see hr.fer.zemris.java.hw06.shell.commands.HelpCommand
     */
    String getCommandName();

    /**
     * Returns an unmodifiable list of lines describing the command and its usage.
     *
     * @return the command description
     *
     * @see hr.fer.zemris.java.hw06.shell.commands.HelpCommand
     */
    List<String> getCommandDescription();

}
