package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

public class SmartScriptLexer {

	private final char[] data;
	private Token token;
	private int currentIndex;
	private LexerState state;

	public SmartScriptLexer(String text) {
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

	public void setState(LexerState state) {
		this.state = Objects.requireNonNull(state, "State of lexer cannot be null.");
	}

	private boolean isValidIndex(int index) {
		return index >= 0 && index < data.length;
	}

	private void skipWhitespace() {
		while (isValidIndex(currentIndex) && Character.isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}

	private Token extractNextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("End of document already reached.");
		}

		if (currentIndex >= data.length) {
			return new Token(TokenType.EOF, null);
		}

		if (state == LexerState.BASIC) {
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
				return new Token(TokenType.EOF, null);
			}

			if (data[currentIndex] == '$') {
				return extractNextTagEndToken();
			}

			if (data[currentIndex] == '-' && isValidIndex(currentIndex + 1) && !isValidNumberCharacter(data[currentIndex + 1])) {
				++currentIndex;

				return new Token(TokenType.OPERATOR, "-");
			}

			if (isValidNumberCharacter(data[currentIndex])) {
				return extractNextNumberToken();
			}

			if (isValidOperatorCharacter(data[currentIndex])) {
				++currentIndex;

				return new Token(TokenType.OPERATOR, String.valueOf(data[currentIndex - 1]));
			}

			if (isValidIdentifierCharacter(data[currentIndex])) {
				return extractNextIdentifierToken();
			}

			if (data[currentIndex] == '"') {
				return extractNextStringToken();
			}

		}

		String message = String.format("Could not tokenise '%c'.", data[currentIndex]);

		throw new LexerException(message);
	}

	private boolean isValidNumberCharacter(char character) {
		return ('0' <= character && character <= '9') || character == '.' || character == '+' || character == '-';
	}

	private boolean isValidOperatorCharacter(char character) {
		return character == '+' || character == '-' || character == '*' || character == '/' || character == '^';
	}

	private boolean isValidIdentifierCharacter(char character) {
		if (Character.isLetter(character)) {
			return true;
		}

		if ('0' <= character && character <= '9') {
			return true;
		}

		return character == '_' || character == '@' || character == '=';
	}

	private Token extractNextTextToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex(currentIndex) && data[currentIndex] != '{') {
			if (data[currentIndex] == '\\') {
				if (isValidIndex(currentIndex + 1) && (data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '{')) {
					sb.append(data[currentIndex + 1]);
				}
				else {
					throw new LexerException("Invalid escaping outside of tag.");
				}

				currentIndex += 2;
				continue;
			}

			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.TEXT, sb.toString());
	}

	private Token extractNextTagStartToken() {
		StringBuilder sb = new StringBuilder();

		if (isValidIndex(currentIndex) && data[currentIndex] == '{') {
			sb.append('{');
			++currentIndex;

			if (isValidIndex(currentIndex) && data[currentIndex] == '$') {
				sb.append('$');
				++currentIndex;
			}
			else {
				throw new LexerException("Invalid start of tag.");
			}
		}

		return new Token(TokenType.TAG_START, sb.toString());
	}

	private Token extractNextTagEndToken() {
		StringBuilder sb = new StringBuilder();

		if (isValidIndex(currentIndex) && data[currentIndex] == '$') {
			sb.append('$');
			++currentIndex;

			if (isValidIndex(currentIndex) && data[currentIndex] == '}') {
				sb.append('}');
				++currentIndex;
			}
			else {
				throw new LexerException("Invalid end of tag.");
			}
		}

		return new Token(TokenType.TAG_START, sb.toString());
	}

	private Token extractNextNumberToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex(currentIndex) && isValidNumberCharacter(data[currentIndex])) {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		String number = sb.toString();

		try {
			int integer = Integer.parseInt(number);

			return new Token(TokenType.INTEGER, integer);
		} catch (NumberFormatException e) {}

		try {
			double doubleNumber = Double.parseDouble(number);

			return new Token(TokenType.DOUBLE, doubleNumber);
		}
		catch (NumberFormatException e) {
			String message = String.format("'%s' is not a valid integer nor a valid double.", number);

			throw new LexerException(message);
		}
	}

	private Token extractNextIdentifierToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex(currentIndex) && isValidIdentifierCharacter(data[currentIndex])) {
			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new Token(TokenType.IDENTIFIER, sb.toString());
	}

	private Token extractNextStringToken() {
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
					throw new LexerException("Invalid escape in tag string.");
				}

				currentIndex += 2;
				continue;
			}

			sb.append(data[currentIndex]);

			++currentIndex;
		}

		++currentIndex;

		return new Token(TokenType.TEXT, sb.toString());
	}

}
