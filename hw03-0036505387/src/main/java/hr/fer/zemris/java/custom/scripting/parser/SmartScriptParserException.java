package hr.fer.zemris.java.custom.scripting.parser;

public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SmartScriptParserException(Exception e) {
		super(e);
	}

	public SmartScriptParserException(String message) {
		super(message);
	}

}
