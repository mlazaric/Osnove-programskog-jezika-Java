package hr.fer.zemris.java.gui.layouts;

/**
 * Exception thrown by {@link CalcLayout} when it encounters an unexpected situation.
 *
 * @author Marko Lazarić
 */
public class CalcLayoutException extends RuntimeException {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -3503721957041493250L;

    /**
     * Creates a new {@link CalcLayoutException} with the given message.
     *
     * @param message the message to pass to super constructor
     */
    public CalcLayoutException(String message) {
        super(message);
    }
}
