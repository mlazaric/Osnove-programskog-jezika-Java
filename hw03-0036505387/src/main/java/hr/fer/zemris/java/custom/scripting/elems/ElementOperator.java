package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

public class ElementOperator extends Element {

	private final String symbol;

	public ElementOperator(String symbol) {
		this.symbol = Objects.requireNonNull(symbol, "Symbol of operator cannot be null");
	}

	@Override
	public String asText() {
		return symbol;
	}

	public String getSymbol() {
		return symbol;
	}
}
