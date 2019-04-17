package hr.fer.zemris.java.hw06.shell;

/**
 * Status objects returned by {@link ShellCommand}.
 *
 * @author Marko Lazarić
 */
public enum ShellStatus {

    /**
     * Returned if the shell should not exit.
     */
    CONTINUE,

    /**
     * Returned if the shell should exit.
     */
    TERMINATE;
}
