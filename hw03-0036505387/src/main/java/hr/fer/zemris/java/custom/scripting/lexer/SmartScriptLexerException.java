package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * An exception thrown in {@link SmartScriptLexer} when it has encountered
 * an error while tokenising the text.
 *
 * @author Marko Lazarić
 *
 */
public class SmartScriptLexerException extends RuntimeException {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link SmartScriptLexerException} and passed the given
	 * {@code message} to the super constructor.
	 */
	public SmartScriptLexerException(String message) {
		super(message);
	}

}
