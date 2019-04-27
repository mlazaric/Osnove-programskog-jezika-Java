package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.ArgumentParser;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Command which prints the contents of a file to the environment's output stream.
 *
 * @author Marko Lazarić
 */
public class CatCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command cat takes one or two arguments.",
                "The first argument is a path to some file and is mandatory.",
                "The second argument is a charset name which is used to interpret chars from bytes.",
                "If not provided, the platform's default charset is used."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
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

        if (args.size() == 1) {
            readFile(env, args.get(0), Charset.defaultCharset());
        }
        else if (args.size() == 2) {
            readFile(env, args.get(0), args.get(1));
        }
        else {
            env.writeln("Cat command expects 1 or 2 arguments, " + args.size() + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Reads the contents of the file using the specified charset.
     *
     * @param env the environment in which this command is executed
     * @param filePath the path to the file being read
     * @param charset the charset used
     */
    private void readFile(Environment env, String filePath, Charset charset) {
        Path path = env.getCurrentDirectory().resolve(filePath);

        if (Files.isDirectory(path)) {
            env.writeln("'" + filePath + "' is a directory");
            return;
        }

        if (!Files.exists(path)) {
            env.writeln("'" + filePath + "' does not exist.");
            return;
        }

        try {
            Files.lines(path, charset).forEach(env::writeln);
        } catch (IOException e) {
            env.writeln("Error while reading from file '" + filePath + "'.");
        }
    }

    /**
     * Reads the contents of the file using the specified charset.
     *
     * @param env the environment in which this command is executed
     * @param filePath the path to the file being read
     * @param charset the charset used
     */
    private void readFile(Environment env, String filePath, String charset) {
        try {
            Charset realCharset = Charset.forName(charset);

            readFile(env, filePath, realCharset);
        }
        catch (IllegalArgumentException e) {
            env.writeln("Invalid charset '" + charset + "'.");
        }
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
