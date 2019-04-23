package hr.fer.zemris.java.custom.scripting.exec;

public class EmptyMultistackEntryException extends RuntimeException {

    static final long serialVersionUID = -7034897190745766939L;

    public EmptyMultistackEntryException() {
        super();
    }

    public EmptyMultistackEntryException(String message) {
        super(message);
    }

    public EmptyMultistackEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyMultistackEntryException(Throwable cause) {
        super(cause);
    }

}
