package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

public class Token {
	private final TokenType type;
	private final Object value;

	public Token(TokenType type, Object value) {
		this.type = Objects.requireNonNull(type, "Type of token cannot be null.");
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public TokenType getType() {
		return type;
	}
}