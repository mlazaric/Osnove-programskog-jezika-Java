package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

public class SmartScriptToken {

	private final SmartScriptTokenType type;
	private final Object value;

	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		this.type = Objects.requireNonNull(type, "Type of token cannot be null.");
		this.value = value;
	}

	public SmartScriptTokenType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}
}
