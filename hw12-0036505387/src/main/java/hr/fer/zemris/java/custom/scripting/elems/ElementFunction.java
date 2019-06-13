package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Represents a function name in an expression for the Smart scripting language.
 *
 * @author Marko Lazarić
 *
 */
public class ElementFunction extends Element {

	/**
	 * The name of the function.
	 */
	private final String name;

	/**
	 * Creates a new {@link ElementFunction} with the given function name.
	 *
	 * @param name the name of the function
	 *
	 * @throws NullPointerException if {@code name} is {@code null}
	 */
	public ElementFunction(String name) {
		this.name = Objects.requireNonNull(name, "Name of function cannot be null.");
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.custom.scripting.elems.Element#asText()
	 */
	@Override
	public String asText() {
		return toString();
	}

	/**
	 * Returns the name of the function.
	 *
	 * @return the value of the function
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "@" + name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name);
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
		if (!(obj instanceof ElementFunction)) {
			return false;
		}

		ElementFunction other = (ElementFunction) obj;

		return Objects.equals(name, other.name);
	}
}
