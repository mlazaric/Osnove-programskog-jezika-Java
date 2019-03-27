package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents an operator in an expression for the Smart scripting language.
 *
 * @author Marko Lazarić
 *
 */
public class ElementOperator extends Element {

	/**
	 * The symbol of the operator.
	 */
	private final String symbol;

	/**
	 * Creates a new {@link ElementOperator} with the given symbol.
	 *
	 * @param symbol the symbol of the operator
	 *
	 * @throws NullPointerException if {@code symbol} is {@code null}
	 */
	public ElementOperator(String symbol) {
		this.symbol = Objects.requireNonNull(symbol, "Symbol of operator cannot be null");
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.elems.Element#asText()
	 */
	@Override
	public String asText() {
		return symbol;
	}

	/**
	 * Returns the operator's symbol.
	 *
	 * @return the symbol of the operator
	 */
	public String getSymbol() {
		return symbol;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ElementOperator [symbol=" + symbol + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(symbol);
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
		if (!(obj instanceof ElementOperator)) {
			return false;
		}

		ElementOperator other = (ElementOperator) obj;

		return Objects.equals(symbol, other.symbol);
	}

}
