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
		if (!(obj instanceof SmartScriptToken)) {
			return false;
		}

		SmartScriptToken other = (SmartScriptToken) obj;

		return type == other.type && Objects.equals(value, other.value);
	}


	@Override
	public String toString() {
		return String.format("Token[type=%s, value='%s']", type, value);
	}

}
