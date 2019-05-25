package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Types of tokens generated by {@link SmartScriptLexer}.
 *
 * @author Marko Lazarić
 *
 */
public enum SmartScriptTokenType {

	/**
	 * Textual data outside of tags.
	 */
	TEXT,

	/**
	 * String literal inside of tag.
	 */
	STRING,

	/**
	 * Integer constant inside of tag.
	 */
	INTEGER,

	/**
	 * Double constant inside of tag.
	 */
	DOUBLE,

	/**
	 * Operator inside of tag.
	 *
	 *  Valid operators are: +, -, *, / and ^.
	 */
	OPERATOR,

	/**
	 * Identifier inside of tag.
	 *
	 * Includes function names, variable names and tag names.
	 * The parser should decided which one it is.
	 */
	IDENTIFIER,

	/**
	 * Start of the tag.
	 *
	 * Contains "{$" as the value.
	 */
	TAG_START,

	/**
	 * End of the tag.
	 *
	 * Contains "$}" as the value.
	 */
	TAG_END,

	/**
	 * End of file reached.
	 *
	 * Contains null as the value.
	 */
	EOF;

}
