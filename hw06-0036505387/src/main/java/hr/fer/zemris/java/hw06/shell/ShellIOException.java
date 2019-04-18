package hr.fer.zemris.java.hw06.shell;

/**
 * Exception thrown by {@link Environment}, used to signify an unrecoverable error.
 *
 * @author Marko Lazarić
 */
public class ShellIOException extends RuntimeException {

    static final long serialVersionUID = 1L;

    /**
     * Creates a {@link ShellIOException} with the given argument as the cause. Used to wrap checked exceptions.
     *
     * @param cause the thrown exception
     */
    public ShellIOException(Throwable cause) {
        super(cause);
    }
}
