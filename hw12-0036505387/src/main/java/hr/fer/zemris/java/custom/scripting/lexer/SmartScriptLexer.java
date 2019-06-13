package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

/**
 * The lexer used for tokenisation of the Smart scripting language.
 *
 * @author Marko Lazarić
 *
 */
public class SmartScriptLexer {

	/**
	 * The input characters to tokenise.
	 */
	private final char[] data;

	/**
	 * The last generated token.
	 */
	private SmartScriptToken token;

	/**
	 * The index of the last unchecked character.
	 */
	private int currentIndex;

	/**
	 * The state of the lexer. The default state is {@link SmartScriptLexerState#BASIC}.
	 *
	 *  @see SmartScriptLexerState
	 */
	private SmartScriptLexerState state;

	/**
	 * Creates a new lexer that tokenises the input string.
	 *
	 * @param text the string to tokenise
	 *
	 * @throws NullPointerException if {@code text} is {@code null}
	 */
	public SmartScriptLexer(String text) {
		Objects.requireNonNull(text, "Text cannot be null.");

		data = text.toCharArray();
		currentIndex = 0;
		state = SmartScriptLexerState.BASIC;
	}

	/**
	 * Generates and returns the next token.
	 *
	 * @return the newly generated token
	 *
	 * @throws SmartScriptLexerException if it is called again after an
	 *                                   {@link SmartScriptTokenType#EOF}
	 *                                    token was returned.
	 */
	public SmartScriptToken nextToken() {
		token = extractNextToken();

		return getToken();
	}

	/**
	 * Returns the last previously generated token.
	 * It does not generate a new token.
	 * It returns {@code null} if called before {@link #nextToken()} since no
	 * tokens have been generated.
	 *
	 * @return the last generated token.
	 */
	public SmartScriptToken getToken() {
		return token;
	}

	/**
	 * Set the state of the lexer.
	 *
	 * @param state the new state of the lexer
	 *
	 * @throws NullPointerException if {@code state} is {@code null}
	 *
	 * @see SmartScriptLexerState
	 */
	public void setState(SmartScriptLexerState state) {
		this.state = Objects.requireNonNull(state, "State of lexer cannot be null.");
	}

	/**
	 * Checks whether {@code index} is a valid index of the {@code data}
	 * array.
	 *
	 * @param index the index to check
	 * @return true if index is a valid index,
	 *         false otherwise
	 */
	private boolean isValidIndex(int index) {
		return index >= 0 && index < data.length;
	}

	/**
	 * Skips all the whitespace characters.
	 *
	 * @see Character#isWhitespace(char)
	 */
	private void skipWhitespace() {
		while (isValidIndex(currentIndex) && Character.isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}

	/**
	 * Extracts a new token.
	 *
	 * @return the newly extracted token
	 *
	 * @throws SmartScriptLexerException if called after it has generated an
	 *                                   {@link SmartScriptTokenType#EOF}
	 *                                   token
	 */
	private SmartScriptToken extractNextToken() {
		if (token != null && token.getType() == SmartScriptTokenType.EOF) {
			throw new SmartScriptLexerException("End of document already reached.");
		}

		if (currentIndex >= data.length) {
			return new SmartScriptToken(SmartScriptTokenType.EOF, null);
		}

		if (state == SmartScriptLexerState.BASIC) {
			if (data[currentIndex] == '{') {
				return extractNextTagStartToken();
			}
			else {
				return extractNextTextToken();
			}
		}
		else {
			skipWhitespace();

			if (!isValidIndex(currentIndex)) {
				return new SmartScriptToken(SmartScriptTokenType.EOF, null);
			}

			if (data[currentIndex] == '$') {
				return extractNextTagEndToken();
			}

			if (data[currentIndex] == '-' && isValidIndex(currentIndex + 1) && !isValidNumberCharacter(data[currentIndex + 1])) {
				++currentIndex;

				return new SmartScriptToken(SmartScriptTokenType.OPERATOR, "-");
			}

			if (isValidNumberCharacter(data[currentIndex])) {
				return extractNextNumberToken();
			}

			if (isValidOperatorCharacter(data[currentIndex])) {
				++currentIndex;

				return new SmartScriptToken(SmartScriptTokenType.OPERATOR, String.valueOf(data[currentIndex - 1]));
			}

			if (isValidIdentifierCharacter(data[currentIndex])) {
				return extractNextIdentifierToken();
			}

			if (data[currentIndex] == '"') {
				return extractNextStringToken();
			}

		}

		String message = String.format("Could not tokenise '%c'.", data[currentIndex]);

		throw new SmartScriptLexerException(message);
	}

	/**
	 * Returns whether {@code character} can be a part of a number in this language.
	 *
	 * @param character the character to check
	 * @return true if it can be a part of a number,
	 *         false otherwise
	 */
	private boolean isValidNumberCharacter(char character) {
		return ('0' <= character && character <= '9') || character == '.' || character == '-';
	}

	/**
	 * Returns whether {@code character} is considered a valid operator in this language.
	 *
	 * @param character the character to check
	 * @return true if is a valid operator,
	 *         false otherwise
	 */
	private boolean isValidOperatorCharacter(char character) {
		return character == '+' || character == '-' || character == '*' || character == '/' || character == '^';
	}

	/**
	 * Returns whether {@code character} can be a part of an identifier in this language.
	 *
	 * @param character the character to check
	 * @return true if it can be a part of an identifier,
	 *         false otherwise
	 */
	private boolean isValidIdentifierCharacter(char character) {
		if (Character.isLetter(character)) {
			return true;
		}

		if ('0' <= character && character <= '9') {
			return true;
		}

		return character == '_' || character == '@' || character == '=';
	}

	/**
	 * Extracts the next {@link SmartScriptTokenType#TEXT} token.
	 *
	 * @return the newly extracted {@link SmartScriptTokenType#TEXT} token
	 *
	 * @throws SmartScriptLexerException if it encounters an invalid escape
	 *                                   sequence
	 */
	private SmartScriptToken extractNextTextToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex(currentIndex) && data[currentIndex] != '{') {
			if (data[currentIndex] == '\\') {
				if (isValidIndex(currentIndex + 1) && (data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '{')) {
					sb.append(data[currentIndex + 1]);
				}
				else {
					throw new SmartScriptLexerException("Invalid escape sequence outside of tag.");
				}

				currentIndex += 2;
				continue;
			}

			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new SmartScriptToken(SmartScriptTokenType.TEXT, sb.toString());
	}

	/**
	 * Extracts the next {@link SmartScriptTokenType#TAG_START} token.
	 * The state should be changed to {@link SmartScriptLexerState#INSIDE_TAG} by the caller.
	 *
	 * @return the newly extracted {@link SmartScriptTokenType#TAG_START} token
	 *
	 * @throws SmartScriptLexerException if { is not followed by a $
	 */
	private SmartScriptToken extractNextTagStartToken() {
		StringBuilder sb = new StringBuilder();

		if (isValidIndex(currentIndex) && data[currentIndex] == '{') {
			sb.append('{');
			++currentIndex;

			if (isValidIndex(currentIndex) && data[currentIndex] == '$') {
				sb.append('$');
				++currentIndex;
			}
			else {
				return new SmartScriptToken(SmartScriptTokenType.TEXT, "{");
				// throw new SmartScriptLexerException("Invalid start of tag.");
			}
		}

		return new SmartScriptToken(SmartScriptTokenType.TAG_START, sb.toString());
	}

	/**
	 * Extracts the next {@link SmartScriptTokenType#TAG_END} token.
	 * The state should be changed to {@link SmartScriptLexerState#BASIC} by the caller.
	 *
	 * @return the newly extracted {@link SmartScriptTokenType#TAG_END} token
	 *
	 * @throws SmartScriptLexerException if $ is not followed by a }
	 */
	private SmartScriptToken extractNextTagEndToken() {
		StringBuilder sb = new StringBuilder();

		if (isValidIndex(currentIndex) && data[currentIndex] == '$') {
			sb.append('$');
			++currentIndex;

			if (isValidIndex(currentIndex) && data[currentIndex] == '}') {
				sb.append('}');
				++currentIndex;
			}
			else {
				throw new SmartScriptLexerException("Invalid end of tag.");
			}
		}

		return new SmartScriptToken(SmartScriptTokenType.TAG_END, sb.toString());
	}

	/**
	 * Extracts the next {@link SmartScriptTokenType#INTEGER} or {@link SmartScriptTokenType#DOUBLE}
	 * token.
	 *
	 * @return the newly extracted number token
	 *
	 * @throws SmartScriptLexerException if it is not a valid integer, nor a valid double
	 */
	private SmartScriptToken extractNextNumberToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex(currentIndex) && isValidNumberCharacter(data[currentIndex])) {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		String number = sb.toString();

		try {
			int integer = Integer.parseInt(number);

			return new SmartScriptToken(SmartScriptTokenType.INTEGER, integer);
		} catch (NumberFormatException e) {}

		try {
			double doubleNumber = Double.parseDouble(number);

			return new SmartScriptToken(SmartScriptTokenType.DOUBLE, doubleNumber);
		}
		catch (NumberFormatException e) {
			String message = String.format("'%s' is not a valid integer nor a valid double.", number);

			throw new SmartScriptLexerException(message);
		}
	}

	/**
	 * Extracts the next {@link SmartScriptTokenType#IDENTIFIER} token.
	 *
	 * @return the newly extracted {@link SmartScriptTokenType#IDENTIFIER} token
	 */
	private SmartScriptToken extractNextIdentifierToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex(currentIndex) && isValidIdentifierCharacter(data[currentIndex])) {
			if (data[currentIndex] == '=') {
				sb.append('=');

				++currentIndex;

				break;
			}

			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, sb.toString());
	}

	/**
	 * Extracts the next {@link SmartScriptTokenType#STRING} token.
	 *
	 * @return the newly extracted {@link SmartScriptTokenType#STRING} token
	 *
	 * @throws SmartScriptLexerException if it encounters an invalid escape sequence
	 */
	private SmartScriptToken extractNextStringToken() {
		StringBuilder sb = new StringBuilder();

		++currentIndex; // Skip "

		while (isValidIndex(currentIndex) && data[currentIndex] != '"') {
			if (data[currentIndex] == '\\') {

				switch (data[currentIndex + 1]) {
				case '\\':
				case '"':
					sb.append(data[currentIndex + 1]);
					break;

				case 'n':
					sb.append('\n');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 't':
					sb.append('\t');
					break;


				default:
					throw new SmartScriptLexerException("Invalid escape sequence in tag string.");
				}

				currentIndex += 2;
				continue;
			}

			sb.append(data[currentIndex]);

			++currentIndex;
		}

		++currentIndex; // Skip "

		return new SmartScriptToken(SmartScriptTokenType.STRING, sb.toString());
	}

}
