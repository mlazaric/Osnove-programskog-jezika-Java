package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Command which lists available charsets.
 *
 * @author Marko Lazarić
 */
public class CharsetsCommand implements ShellCommand {

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command charsets takes zero arguments.",
                "Lists the names of the supported charsets for your Java platform.",
                "A single charset name is written per line."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (!arguments.isBlank()) {
            env.writeln("Charsets command expects 0 arguments.");
            return ShellStatus.CONTINUE;
        }

        Set<String> charsets = Charset.availableCharsets().keySet();

        for (String charset : charsets) {
            env.writeln(charset);
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

}
