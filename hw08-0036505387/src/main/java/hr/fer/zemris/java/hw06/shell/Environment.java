package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Models a simple environment in which commands can be executed.
 *
 * @author Marko Lazarić
 */
public interface Environment {

    /**
     * Reads a line from input and returns it.
     *
     * @return the read line from the input stream
     *
     * @throws ShellIOException if any exceptions are thrown during the reading of the line
     */
    String readLine() throws ShellIOException;

    /**
     * Writes a {@link String} to the output stream.
     *
     * @param text the {@link String} to write to the output stream
     *
     * @throws ShellIOException if any exceptions are thrown during the writing of the {@link String}
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes a {@link String} to the output stream and writes a newline character to the output stream.
     *
     * @param text the {@link String} to write to the output stream
     *
     * @throws ShellIOException if any exceptions are thrown during the writing of the {@link String}
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns a sorted mapping of command names to command objects.
     *
     * @return the mapping of command names to command objects
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Returns the multiline symbol.
     *
     * @return the multiline symbol
     */
    Character getMultilineSymbol();

    /**
     * Sets the multiline symbol to the given argument.
     *
     * @param symbol the new multiline symbol
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Returns the prompt symbol.
     *
     * @return the prompt symbol
     */
    Character getPromptSymbol();

    /**
     * Sets the prompt symbol to the given argument.
     *
     * @param symbol the new prompt symbol
     */
    void setPromptSymbol(Character symbol);

    /**
     * Returns the morelines symbol.
     *
     * @return the morelines symbol
     */
    Character getMorelinesSymbol();

    /**
     * Sets the morelines symbol to the given argument.
     *
     * @param symbol the new morelines symbol
     */
    void setMorelinesSymbol(Character symbol);

    Path getCurrentDirectory();

    void setCurrentDirectory(Path path);

    Object getSharedData(String key);

    void setSharedData(String key, Object value);

}
