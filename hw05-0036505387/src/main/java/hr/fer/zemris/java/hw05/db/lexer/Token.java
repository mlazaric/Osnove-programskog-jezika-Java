package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * Represents a token created by {@link QueryLexer}.
 *
 * @author Marko Lazarić
 *
 */
public class Token {

	/**
	 * The type of the token.
	 */
	private final TokenType type;

	/**
	 * The value of the token.
	 */
	private final String value;

	/**
	 * Creates a new {@link Token} with the given arguments.
	 *
	 * @param type the type of the token
	 * @param value the value of the token
	 */
	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns the type of the token.
	 *
	 * @return the type of the token
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Returns the value of the token.
	 *
	 * @return the value of the token
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode(java.lang.Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(type, value);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	@Override
	public String toString() {
		return "Token [type=" + type + ", value=" + value + "]";
	}

}
