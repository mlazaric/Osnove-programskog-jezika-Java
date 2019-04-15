package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

import hr.fer.zemris.java.hw05.db.QueryParser;

/**
 * A simple lexer for {@link QueryParser}.
 *
 * @author Marko Lazarić
 *
 */
public class QueryLexer {

	/**
	 * The input data to tokenise.
	 */
	private final char[] data;

	/**
	 * The index of the current character.
	 */
	private int currentIndex;

	/**
	 * The last generated token.
	 */
	private Token token;

	/**
	 * Creates a new {@link QueryLexer} from the given input.
	 *
	 * @param input the text to tokenise
	 *
	 * @throws NullPointerException if {@code text} is {@code null}
	 */
	public QueryLexer(String input) {
		data = Objects.requireNonNull(input, "Cannot tokenise null.").strip().toCharArray();
	}

	/**
	 * Returns the last generated token.
	 *
	 * @return the last generated token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Generates a new token and returns it.
	 *
	 * @return the newly generated token
	 *
	 * @throws IllegalArgumentException if it has already reached end of input or it cannot tokenise a character
	 */
	public Token nextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new IllegalArgumentException("Already reached end of input.");
		}

		skipWhitespace();

		if (!isValidIndex()) {
			token = new Token(TokenType.EOF, null);
		}
		else if (data[currentIndex] == '"') {
			token = extractStringToken();
		}
		else if (isOperator()) {
			token = extractOperatorToken();
		}
		else if (Character.isLetter(data[currentIndex])) {
			token = extractWordToken();
		}
		else {
			throw new IllegalArgumentException("Could not tokenise character '" + data[currentIndex] + "' at index " + currentIndex + ".");
		}

		return token;
	}

	/**
	 * Extracts a {@link TokenType#STRING} token and returns it.
	 *
	 * @return the newly extracted string token
	 *
	 * @throws IllegalArgumentException if the string was not closed
	 */
	private Token extractStringToken() {
		StringBuilder sb = new StringBuilder();

		++currentIndex;

		while (isValidIndex() && data[currentIndex] != '"') {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		if (!isValidIndex()) {
			throw new IllegalArgumentException("String was not closed.");
		}

		++currentIndex;

		return new Token(TokenType.STRING, sb.toString());
	}

	/**
	 * Extracts an {@link TokenType#OPERATOR} token and returns it.
	 *
	 * @return the newly extracted operator token
	 */
	private Token extractOperatorToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex() && isOperator()) {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.OPERATOR, sb.toString());
	}

	/**
	 * Extracts a {@link TokenType#WORD} token and returns it.
	 *
	 * @return the newly extracted word token
	 */
	private Token extractWordToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex() && Character.isLetter(data[currentIndex])) {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.WORD, sb.toString());
	}

	/**
	 * Skips all whitespace characters.
	 */
	private void skipWhitespace() {
		while (isValidIndex() && Character.isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}

	/**
	 * Returns whether the current character is an operator.
	 *
	 * @return whether the current character is an operator
	 */
	private boolean isOperator() {
		return data[currentIndex] == '<' || data[currentIndex] == '>' || data[currentIndex] == '=' || data[currentIndex] == '!';
	}

	/**
	 * Returns whether {@link #currentIndex} is a valid index.
	 *
	 * @return whether it is a valid index
	 */
	private boolean isValidIndex() {
		return currentIndex < data.length;
	}

}
