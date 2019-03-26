package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

public class SmartScriptLexer {

	private final char[] data;
	private SmartScriptToken token;
	private int currentIndex;
	private SmartScriptLexerState state;

	public SmartScriptLexer(String text) {
		Objects.requireNonNull(text, "Text cannot be null.");

		data = text.toCharArray();
		currentIndex = 0;
		state = SmartScriptLexerState.BASIC;
	}

	public SmartScriptToken nextToken() {
		token = extractNextToken();

		return getToken();
	}

	public SmartScriptToken getToken() {
		return token;
	}

	public void setState(SmartScriptLexerState state) {
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

	private boolean isValidNumberCharacter(char character) {
		return ('0' <= character && character <= '9') || character == '.' || character == '-';
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

	private SmartScriptToken extractNextTextToken() {
		StringBuilder sb = new StringBuilder();

		while (isValidIndex(currentIndex) && data[currentIndex] != '{') {
			if (data[currentIndex] == '\\') {
				if (isValidIndex(currentIndex + 1) && (data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '{')) {
					sb.append(data[currentIndex + 1]);
				}
				else {
					throw new SmartScriptLexerException("Invalid escaping outside of tag.");
				}

				currentIndex += 2;
				continue;
			}

			sb.append(data[currentIndex]);

			++currentIndex;
		}

		return new SmartScriptToken(SmartScriptTokenType.TEXT, sb.toString());
	}

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
				throw new SmartScriptLexerException("Invalid start of tag.");
			}
		}

		return new SmartScriptToken(SmartScriptTokenType.TAG_START, sb.toString());
	}

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
					throw new SmartScriptLexerException("Invalid escape in tag string.");
				}

				currentIndex += 2;
				continue;
			}

			sb.append(data[currentIndex]);

			++currentIndex;
		}

		++currentIndex;

		return new SmartScriptToken(SmartScriptTokenType.STRING, sb.toString());
	}

}
