package hr.fer.zemris.java.custom.collections;

/**
 * An exception thrown when
 *
 * @author Marko Lazarić
 *
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmptyStackException() {
		super();
	}

	public EmptyStackException(String message) {
		super(message);
	}

}
