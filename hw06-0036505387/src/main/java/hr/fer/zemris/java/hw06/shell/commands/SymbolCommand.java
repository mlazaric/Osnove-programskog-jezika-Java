package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.*;

/**
 * Command to change or print the symbols used in {@link Environment}.
 *
 * @author Marko Lazarić
 */
public class SymbolCommand implements ShellCommand {

    private final static Map<String, SymbolSetter> SYMBOL_SETTERS;
    private final static Map<String, SymbolGetter> SYMBOL_GETTERS;

    static {
        SYMBOL_SETTERS = new HashMap<>();
        SYMBOL_GETTERS = new HashMap<>();

        SYMBOL_SETTERS.put("PROMPT", Environment::setPromptSymbol);
        SYMBOL_GETTERS.put("PROMPT", Environment::getPromptSymbol);

        SYMBOL_SETTERS.put("MORELINES", Environment::setMorelinesSymbol);
        SYMBOL_GETTERS.put("MORELINES", Environment::getMorelinesSymbol);

        SYMBOL_SETTERS.put("MULTILINE", Environment::setMultilineSymbol);
        SYMBOL_GETTERS.put("MULTILINE", Environment::getMultilineSymbol);
    }

    /**
     * Static constant unmodifiable list containing lines describing this command.
     *
     * @see #getCommandDescription()
     */
    private static final List<String> COMMAND_DESCRIPTION;

    static {
        List<String> description = Arrays.asList(
                "Command symbol takes one or two arguments.",
                "The first argument is the name of the symbol and it is mandatory.",
                "Valid symbol names are: PROMPT, MORELINES and MULTILINE.",
                "The second argument is optional and represents the new character for that symbol.",
                "",
                "If one argument was given, it will print the current value of that symbol.",
                "If two arguments were given, it will set the value of that symbol to the second argument."
        );

        COMMAND_DESCRIPTION = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] parts = arguments.split("\\s");

        if (parts.length == 1) {
            getSymbol(env, parts[0]);
        }
        else if (parts.length == 2) {
            setSymbol(env, parts[0], parts[1]);
        }
        else {
            env.writeln("Symbol command expects 1 or 2 arguments, " + parts.length + " were given.");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Returns the current value of the specified symbol in the environment.
     *
     * @param env the environment whose symbol it should get
     * @param symbolName the name of the symbol to get
     */
    private void getSymbol(Environment env, String symbolName) {
        SymbolGetter getter = SYMBOL_GETTERS.get(symbolName);

        // Check if it is a valid symbol name
        if (getter == null) {
            env.writeln("'" + symbolName + "' is not a valid symbol name, valid symbol names are: PROMPT, MORELINES and MULTILINE.");

            return;
        }

        env.writeln("Symbol for " + symbolName + " is '" + getter.getSymbol(env) + "'");
    }

    /**
     * Sets the specified symbol in the environment to the new value.
     *
     * @param env the environment whose symbol it should set
     * @param symbolName the name of the symbol to set
     * @param replacementSymbol the new value of that symbol
     */
    private void setSymbol(Environment env, String symbolName, String replacementSymbol) {
        SymbolSetter setter = SYMBOL_SETTERS.get(symbolName);
        SymbolGetter getter = SYMBOL_GETTERS.get(symbolName);

        // Check if it is a valid symbol name
        if (setter == null || getter == null) {
            env.writeln("'" + symbolName + "' is not a valid symbol name, valid symbol names are: PROMPT, MORELINES and MULTILINE.");

            return;
        }

        // Check if the replacementSymbol is exactly one character
        if (replacementSymbol.length() != 1) {
            env.writeln("'" + replacementSymbol + "' is not a valid symbol, it contains more than one character.");

            return;
        }

        char oldSymbol = getter.getSymbol(env);

        setter.setSymbol(env, replacementSymbol.charAt(0));
        env.writeln("Symbol for PROMPT changed from '" + oldSymbol + "' to '" + replacementSymbol + "'");
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }

    /**
     * Models a simple setter for symbols in {@link Environment}.
     *
     * @author Marko Lazarić
     */
    @FunctionalInterface
    private interface SymbolSetter {

        /**
         * Sets the specified symbol to the new value.
         *
         * @param environment the environment whose symbol should be set
         * @param newSymbol the new symbol
         */
        void setSymbol(Environment environment, Character newSymbol);

    }

    /**
     * Models a simple getter for symbols in {@link Environment}.
     *
     * @author Marko Lazarić
     */
    @FunctionalInterface
    private interface SymbolGetter {

        /**
         * Returns the current value of the specified symbol.
         *
         * @param environment the environment whose symbol should be gotten
         * @return the current value of the specified symbol
         */
        Character getSymbol(Environment environment);

    }

}
