package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * States of the {@link SmartScriptLexer}.
 *
 * @author Marko Lazarić
 *
 */
public enum SmartScriptLexerState {
	/**
	 * The default state. Parses everything as text until it encounters a tag start.
	 */
	BASIC,

	/**
	 * The state used for parsing {@link Element}s inside tags.
	 */
	INSIDE_TAG;
}
