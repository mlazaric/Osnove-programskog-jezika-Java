package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

public class Token {

	private final TokenType type;
	private final String value;

	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Token)) {
			return false;
		}

		Token other = (Token) obj;

		return type == other.type && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "Token [type=" + type + ", value=" + value + "]";
	}

}
