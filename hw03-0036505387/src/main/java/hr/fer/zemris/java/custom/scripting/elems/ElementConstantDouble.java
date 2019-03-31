package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents a {@link Double} constant in an expression for the Smart scripting language.
 *
 * @author Marko Lazarić
 *
 */
public class ElementConstantDouble extends Element {

	/**
	 * The {@link Double} constant this element represents.
	 */
	private final double value;

	/**
	 * Create a new {@link ElementConstantDouble} with the given value.
	 *
	 * @param value the value of this element
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.elems.Element#asText()
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}

	/**
	 * Returns the value of this element.
	 *
	 * @return the value of this element
	 */
	public double getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Double.toString(value);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value);
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
		if (!(obj instanceof ElementConstantDouble)) {
			return false;
		}

		ElementConstantDouble other = (ElementConstantDouble) obj;

		return Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}

}
