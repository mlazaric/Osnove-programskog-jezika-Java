package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.regex.Matcher;

/**
 * Models a single filtered result.
 *
 * @author Marko Lazarić
 */
public class FilterResult {

    /**
     * The matcher containing the captured groups.
     */
    private final Matcher matcher;

    /**
     * Creates a new {@link FilterResult} with the given arguments.
     *
     * @param matcher the matcher containing the captured groups
     */
    public FilterResult(Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public String toString() {
        return matcher.group(0);
    }

    /**
     * Returns the number of captured groups.
     *
     * @return the number of captured groups
     */
    public int numberOfGroups() {
        return matcher.groupCount();
    }

    /**
     * Returns the group with the specified index.
     *
     * @param index the index of the group
     * @return the group with the specified index
     */
    public String group(int index) {
        return matcher.group(index);
    }
}
