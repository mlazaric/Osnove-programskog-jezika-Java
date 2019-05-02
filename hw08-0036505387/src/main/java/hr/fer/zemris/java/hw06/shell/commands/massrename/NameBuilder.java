package hr.fer.zemris.java.hw06.shell.commands.massrename;

@FunctionalInterface
public interface NameBuilder {

    void execute(FilterResult result, StringBuilder sb);

}
