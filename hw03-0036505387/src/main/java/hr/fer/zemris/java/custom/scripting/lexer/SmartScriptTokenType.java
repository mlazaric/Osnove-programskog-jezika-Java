package hr.fer.zemris.java.custom.scripting.lexer;

public enum SmartScriptTokenType {
	TEXT,

	STRING, INTEGER, DOUBLE, OPERATOR,

	IDENTIFIER,

	TAG_START, TAG_END, EOF;
}
