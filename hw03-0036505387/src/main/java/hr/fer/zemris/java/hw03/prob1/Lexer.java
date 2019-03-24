package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

public class Lexer {
	private char[] data;
	private Token token;
	private int currentIndex;
	private LexerState state;

	public Lexer(String text) {
		Objects.requireNonNull(text, "Text cannot be null.");

		data = text.toCharArray();
		currentIndex = 0;
		state = LexerState.BASIC;
	}

	public Token nextToken() {
		token = extractNextToken();

		return getToken();
	}

	public Token getToken() {
		return token;
	}

	private void skipWhitespace() {
		while (currentIndex != data.length &&
				Character.isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}

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

	private boolean isValidPartOfWord(char character) {
		return Character.isLetter(character) || character == '\\';
	}

	private boolean isDigit(char character) {
		return character >= '0' && character <= '9';
	}

	private Token extractNextWord() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && isValidPartOfWord(data[currentIndex])) {
			if (data[currentIndex] == '\\') {
				if (data.length > (currentIndex + 1) &&
						(data[currentIndex + 1] == '\\' ||
						isDigit(data[currentIndex + 1]))) {

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

	private Token extractNextSymbol() {
		++currentIndex;

		return new Token(TokenType.SYMBOL, data[currentIndex - 1]);
	}

	private Token extractNextExtendedToken() {
		StringBuilder sb = new StringBuilder();

		while (currentIndex < data.length && !Character.isWhitespace(data[currentIndex]) && data[currentIndex] != '#') {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.WORD, sb.toString());
	}

	public LexerState getState() {
		return state;
	}

	public void setState(LexerState state) {
		this.state = Objects.requireNonNull(state, "Cannot set lexer state to null.");
	}


}