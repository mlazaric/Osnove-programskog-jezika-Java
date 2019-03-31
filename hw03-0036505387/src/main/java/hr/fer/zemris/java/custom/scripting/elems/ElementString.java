package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents a {@link String} constant in an expression for the Smart scripting language.
 *
 * @author Marko Lazarić
 *
 */
public class ElementString extends Element {

	/**
	 * The {@link String} constant this element represents.
	 */
	private final String value;

	/**
	 * Creates a new {@link ElementString} with the given value.
	 *
	 * @param value the value of this element
	 *
	 * @throws NullPointerException if {@code value} is {@code null}
	 */
	public ElementString(String value) {
		this.value = Objects.requireNonNull(value, "Value of string element cannot be null.");
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.elems.Element#asText()
	 */
	@Override
	public String asText() {
		return value;
	}

	/**
	 * Returns the value of this element.
	 *
	 * @return the value of this element
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String value = this.value.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\n", "\\n")
				.replace("\t", "\\t")
				.replace("\r", "\\r");

		return "\"" + value + "\"";
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
		if (!(obj instanceof ElementString)) {
			return false;
		}

		ElementString other = (ElementString) obj;

		return Objects.equals(value, other.value);
	}

}
