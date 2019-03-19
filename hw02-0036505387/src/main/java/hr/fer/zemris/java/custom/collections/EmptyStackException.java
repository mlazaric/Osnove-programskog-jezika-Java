package hr.fer.zemris.java.custom.collections;

import java.io.Serializable;

/**
 * An exception thrown when illegal operations are performed on an empty stack.
 *
 * Some of the illegal operations to perform on an empty stack are: peeking or
 * popping the top element.
 *
 * @author Marko Lazarić
 *
 */
public class EmptyStackException extends RuntimeException {

	/**
	 * Used by {@link Serializable}.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link EmptyStackException} with no message.
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Creates a new {@link EmptyStackException} with the given message.
	 *
	 * @param message the message to pass to {@link RuntimeException}
	 */
	public EmptyStackException(String message) {
		super(message);
	}

}
