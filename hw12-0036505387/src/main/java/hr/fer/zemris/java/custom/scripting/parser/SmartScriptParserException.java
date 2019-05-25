package hr.fer.zemris.java.custom.scripting.parser;

/**
 * An exception thrown in {@link SmartScriptParser} when it has encountered
 * an error while parsing the text.
 *
 * @author Marko Lazarić
 *
 */
public class SmartScriptParserException extends RuntimeException {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a {@link SmartScriptParserException} by wrapping the {@link Exception}.
	 *
	 * @param e the exception to wrap
	 */
	public SmartScriptParserException(Exception e) {
		super(e);
	}

	/**
	 * Creates a {@link SmartScriptParserException} with the given message.
	 *
	 * @param message the message to pass to super
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

}
