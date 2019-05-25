package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents an {@link Integer} constant in an expression for the Smart scripting language.
 *
 * @author Marko Lazarić
 *
 */
public class ElementConstantInteger extends Element {

	/**
	 * The {@link Integer} constant this element represents.
	 */
	private final int value;

	/**
	 * Create a new {@link ElementConstantInteger} with the given value.
	 *
	 * @param value the value of this element
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.elems.Element#asText()
	 */
	@Override
	public String asText() {
		return Integer.toString(value);
	}

	/**
	 * Returns the value of this element.
	 *
	 * @return the value of this element
	 */
	public int getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Integer.toString(value);
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
		if (!(obj instanceof ElementConstantInteger)) {
			return false;
		}

		ElementConstantInteger other = (ElementConstantInteger) obj;

		return value == other.value;
	}



}
