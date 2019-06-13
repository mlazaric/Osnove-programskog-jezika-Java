package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * States of the {@link Lexer}.
 *
 * @author Marko Lazarić
 *
 */
public enum LexerState {

	/**
	 * The default state in which it tokenises words, symbols and numbers individually.
	 */
	BASIC,


	/**
	 * The state it should enter after it tokenises a # and which it should leave after it
	 * receives another #.<br/>
	 * <br/>
	 * In this state everything is considered one big "word".
	 */
	EXTENDED;

}
