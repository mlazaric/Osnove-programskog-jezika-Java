package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

public class QueryLexer {

	private final char[] data;
	private int currentIndex;
	private Token token;

	public QueryLexer(String input) {
		data = Objects.requireNonNull(input, "Cannot tokenise null.").strip().toCharArray();
	}

	public Token getToken() {
		return token;
	}

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

	private Token extractStringToken() {
		StringBuilder sb = new StringBuilder();

		++currentIndex;

		while (isValidIndex() && data[currentIndex] != '"') {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		if (data[currentIndex] != '"') {
			throw new IllegalArgumentException("String was not closed.");
		}

		++currentIndex;

		return new Token(TokenType.STRING, sb.toString());
	}

	private Token extractOperatorToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex() && isOperator()) {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.OPERATOR, sb.toString());
	}

	private Token extractWordToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex() && Character.isLetter(data[currentIndex])) {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.WORD, sb.toString());
	}

	private void skipWhitespace() {
		while (isValidIndex() && Character.isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}

	private boolean isOperator() {
		return data[currentIndex] == '<' || data[currentIndex] == '>' || data[currentIndex] == '=' || data[currentIndex] == '!';
	}

	private boolean isValidIndex() {
		return currentIndex < data.length;
	}

}
