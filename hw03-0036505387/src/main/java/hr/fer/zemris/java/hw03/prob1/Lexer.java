package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * A simple lexer for the language specified in the second task.
 *
 * @author Marko Lazarić
 *
 */
public class Lexer {

	/**
	 * The data to tokenise.
	 */
	private char[] data;

	/**
	 * Current token it extracted.
	 */
	private Token token;

	/**
	 * Index of {@link #data} it has tokenised to.
	 */
	private int currentIndex;

	/**
	 * State of the lexer. By default it is set to {@link LexerState#BASIC}.
	 *
	 *  @see LexerState
	 */
	private LexerState state;

	/**
	 * Constructs a new lexer instance and tokenises the given {@code text}.
	 *
	 * @param text the data to tokenise
	 *
	 * @throws NullPointerException if {@code text} is {@code null}
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text, "Text cannot be null.");

		data = text.toCharArray();
		currentIndex = 0;
		state = LexerState.BASIC;
	}

	/**
	 * Extract the next token from the data.
	 *
	 * @return the newly generated token
	 *
	 * @throws LexerException if called after it has generated an EOF token
	 */
	public Token nextToken() {
		token = extractNextToken();

		return getToken();
	}

	/**
	 * Get the last generated token.
	 *
	 * @return the last generated token
	 *
	 * @see #token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Skip all whitespace characters.
	 */
	private void skipWhitespace() {
		while (currentIndex != data.length &&
				Character.isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}

	/**
	 * Extracts a new token from the data.
	 *
	 * @return the newly extracted token
	 *
	 * @throws LexerException if called after it has generated an EOF token
	 */
	private Token extractNextToken() {
		if (token != null &&
				token.getType() == TokenType.EOF) {
			throw new LexerException("Already reached end of file.");
		}

		skipWhitespace();

		if (currentIndex == data.length) {
			return new Token(TokenType.EOF, null);
		}

		if (state == LexerState.EXTENDED && data[currentIndex] != '#') {
			return extractNextExtendedToken();
		}

		if (isValidPartOfWord(data[currentIndex])) {
			return extractNextWord();
		}

		if (isDigit(data[currentIndex])) {
			return extractNextNumber();
		}

		return extractNextSymbol();
	}

	/**
	 * Determines if {@code character} could be a part of a word.
	 *
	 * @param character the character to check
	 * @return true if it is a letter or a backslash,
	 *         false otherwise
	 */
	private boolean isValidPartOfWord(char character) {
		return Character.isLetter(character) || character == '\\';
	}

	/**
	 * Determines if {@code character} is a digit.
	 *
	 * @param character the character to check
	 * @return true if it is a digit,
	 *         false otherwise
	 */
	private boolean isDigit(char character) {
		return character >= '0' && character <= '9';
	}

	/**
	 * Extracts a new word token.
	 *
	 * @return the newly extracted word token
	 *
	 * @throws LexerException if there it runs into an invalid escape sequence
	 *
	 * @see TokenType#WORD
	 * @see #isValidPartOfWord(char)
	 */
	private Token extractNextWord() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && isValidPartOfWord(data[currentIndex])) {
			if (data[currentIndex] == '\\') {
				if (data.length > (currentIndex + 1) &&
						(data[currentIndex + 1] == '\\' || isDigit(data[currentIndex + 1]))) {

					sb.append(data[currentIndex + 1]);

					currentIndex += 2;

					continue;
				}
				else {
					throw new LexerException("Invalid escape sequence.");
				}
			}

			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.WORD, sb.toString());
	}

	/**
	 * Extracts a new number token.
	 *
	 * @return the newly extracted number token
	 *
	 * @throws LexerException if the number does not fit into a long
	 *
	 * @see TokenType#NUMBER
	 * @see #isDigit(char)
	 */
	private Token extractNextNumber() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && isDigit(data[currentIndex])) {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		try {
			long number = Long.parseLong(sb.toString());

			return new Token(TokenType.NUMBER, number);
		} catch (NumberFormatException e) {
			String message = String.format("'%s' is not a valid number.", sb.toString());

			throw new LexerException(message);
		}
	}

	/**
	 * Extracts a new symbol token.
	 *
	 * @return the newly extracted symbol token
	 *
	 * @see TokenType#SYMBOL
	 */
	private Token extractNextSymbol() {
		++currentIndex;

		return new Token(TokenType.SYMBOL, data[currentIndex - 1]);
	}

	/**
	 * Extracts a new word token in the extended state.
	 *
	 * @return the newly extracted word token
	 *
	 * @see TokenType#WORD
	 * @see LexerState#EXTENDED
	 */
	private Token extractNextExtendedToken() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && !Character.isWhitespace(data[currentIndex]) && data[currentIndex] != '#') {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.WORD, sb.toString());
	}

	/**
	 * Returns the current state of the lexer.
	 *
	 * @return the current state of the lexer
	 */
	public LexerState getState() {
		return state;
	}

	/**
	 * Sets the state of the lexer.
	 *
	 * @param state the new state of the lexer
	 *
	 * @throws NullPointerException if {@code state} is {@code null}
	 */
	public void setState(LexerState state) {
		this.state = Objects.requireNonNull(state, "Cannot set lexer state to null.");
	}


}