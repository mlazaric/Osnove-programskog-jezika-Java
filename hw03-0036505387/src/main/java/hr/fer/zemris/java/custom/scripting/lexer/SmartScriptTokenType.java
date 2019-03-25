package hr.fer.zemris.java.custom.scripting.lexer;

public enum SmartScriptTokenType {
	INTEGER, DOUBLE, TEXT, OPERATOR, EOF,

	IDENTIFIER,

	TAG_START, TAG_END;
}
