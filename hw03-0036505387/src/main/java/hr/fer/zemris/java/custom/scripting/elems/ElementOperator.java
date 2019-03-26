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

	@Override
	public String toString() {
		return "ElementOperator [symbol=" + symbol + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ElementOperator)) {
			return false;
		}

		ElementOperator other = (ElementOperator) obj;

		return Objects.equals(symbol, other.symbol);
	}


}
