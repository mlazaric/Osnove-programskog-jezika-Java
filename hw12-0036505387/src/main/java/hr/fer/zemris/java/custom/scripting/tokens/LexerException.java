package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Used for exceptional situations tokenisation in {@link Lexer}.
 *
 * @author Marko Lazarić
 *
 */
public class LexerException extends RuntimeException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@link LexerException} with the given message.
	 *
	 * @param message the message to pass to super
	 */
	public LexerException(String message) {
		super(message);
	}

}
