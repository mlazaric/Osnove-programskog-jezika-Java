package hr.fer.zemris.java.hw06.shell.commands.massrename;

/**
 * Models a name builder based on {@link FilterResult}.
 *
 * @author Marko Lazarić
 */
@FunctionalInterface
public interface NameBuilder {

    /**
     * Use the {@link FilterResult} to build the new name of the file and append the result to {@link StringBuilder}.
     *
     * @param result the filtered result
     * @param sb the new name of the file
     */
    void execute(FilterResult result, StringBuilder sb);

}
