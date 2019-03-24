package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

public class Token {

	private final TokenType type;
	private final Object value;

	public Token(TokenType type, Object value) {
		this.type = Objects.requireNonNull(type, "Type of token cannot be null.");
		this.value = value;
	}

	public TokenType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}
}
