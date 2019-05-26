package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Exception thrown by {@link ObjectMultistack} when an empty {@link hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack.MultistackEntry}
 * is popped or peeked.
 *
 * @author Marko Lazarić
 */
public class EmptyMultistackEntryException extends RuntimeException {

    static final long serialVersionUID = 1L;

    /**
     * Creates an {@link EmptyMultistackEntryException} with no message or cause.
     */
    public EmptyMultistackEntryException() {
        super();
    }

    /**
     * Creates an {@link EmptyMultistackEntryException} with the given message.
     *
     * @param message the message to pass to the super constructor
     */
    public EmptyMultistackEntryException(String message) {
        super(message);
    }

    /**
     * Creates an {@link EmptyMultistackEntryException} with the given message and cause.
     *
     * @param message the message to pass to the super constructor
     * @param cause the cause to pass to the super constructor
     */
    public EmptyMultistackEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an {@link EmptyMultistackEntryException} with the given cause.
     *
     * @param cause the cause to pass to the super constructor
     */
    public EmptyMultistackEntryException(Throwable cause) {
        super(cause);
    }

}
