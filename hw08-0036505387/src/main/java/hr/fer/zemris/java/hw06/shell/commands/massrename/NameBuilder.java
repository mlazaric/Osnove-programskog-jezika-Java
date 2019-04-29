package hr.fer.zemris.java.hw06.shell.commands.massrename;

import java.util.Objects;

@FunctionalInterface
public interface NameBuilder {

    void execute(FilterResult result, StringBuilder sb);

}
