package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.regex.Matcher;

public class FilterResult {
    private final Matcher matcher;

    public FilterResult(Matcher matcher) {
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
